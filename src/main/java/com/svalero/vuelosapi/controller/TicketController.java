package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.domain.Ticket;
import com.svalero.vuelosapi.dto.TicketFlightOutDto;
import com.svalero.vuelosapi.dto.TicketOutDto;
import com.svalero.vuelosapi.dto.TicketPassengerOutDto;
import com.svalero.vuelosapi.dto.TicketPatchDto;
import com.svalero.vuelosapi.exceptions.TicketNotFoundException;
import com.svalero.vuelosapi.service.PassengerService;
import com.svalero.vuelosapi.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private PassengerService passengerService;
    private Logger logger = LoggerFactory.getLogger(TicketController.class);

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAll(@Valid @RequestParam(defaultValue = "") String seatNumber,
                                               @RequestParam(defaultValue = "0") int baggage,
                                               @RequestParam(required = false) LocalDate issuing) {
        logger.info("ini GET /tickets by parameters: seatNumber={}, premium={}, issuing={}", seatNumber, baggage, issuing);
        List<Ticket> ticketList = ticketService.findAll();

        if (!seatNumber.isEmpty()) {
            ticketList = ticketList.stream()
                    .filter(ticket -> ticket.getSeatNumber().contains(seatNumber))
                    .collect(Collectors.toList());
        }
        if (baggage > 0) {
            ticketList = ticketList.stream()
                    .filter(ticket -> ticket.getBaggage() == baggage)
                    .collect(Collectors.toList());
        }
        if (issuing != null) {
            ticketList = ticketList.stream()
                    .filter(ticket -> ticket.getIssuing().isEqual(issuing))
                    .collect(Collectors.toList());
        }
        logger.info("end GET /tickets . List size: {}", ticketList.size());
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable long ticketId) throws TicketNotFoundException {
        logger.info("ini GET/ticket/" + ticketId);
        Optional<Ticket> optionalTicket = ticketService.findById(ticketId);
        Ticket ticket = optionalTicket.orElseThrow(() -> new TicketNotFoundException(ticketId));
        logger.info("end GET/ticket/" + ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}/tickets")
    public ResponseEntity<List<TicketPassengerOutDto>> getTicketByPassenger(@PathVariable long passengerId) {
        logger.info("ini GET/passenger/ " + passengerId + "/tickets");
        try {
            List<TicketPassengerOutDto> tickets = ticketService.findTicketByPassenger(passengerId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (TicketNotFoundException e) {
            logger.warn("TicketNotFoundException ID: " + passengerId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Passenger not found with ID: " + passengerId, e);
        } finally {
            logger.info("end GET/passenger/ " + passengerId + "/tickets");
        }
    }
    @GetMapping("/flight/{flightId}/tickets")
    public ResponseEntity<List<TicketFlightOutDto>> getTicketByFlight(@PathVariable long flightId) {
        logger.info("ini GET/flight/ " + flightId + "/tickets");
        try {
            List<TicketFlightOutDto> tickets = ticketService.findTicketByFlight(flightId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (TicketNotFoundException e) {
            logger.warn("TicketNotFoundException ID: " + flightId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Flight not found with ID: " + flightId, e);
        } finally {
            logger.info("end GET/flight/ " + flightId + "/tickets");
        }
    }

    @PostMapping("/ticket")
    public ResponseEntity<TicketOutDto> saveTicket(@Valid @RequestBody Ticket ticket) {
        logger.info("ini Post /ticket" + ticket);
        TicketOutDto newTicket = ticketService.saveTicket(ticket);
        logger.info("end Post /ticket CREATED: {}", newTicket);
        return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
    }

    @DeleteMapping("/ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable long ticketId) throws TicketNotFoundException {
        logger.info("ini DELETE /ticket/" + ticketId);
        ticketService.removeTicket(ticketId);
        logger.info("end DELETE /ticket/" + ticketId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/ticket/{ticketId}")
    public ResponseEntity<Void> modifyTicket(@Valid @RequestBody Ticket ticket, @PathVariable long ticketId) throws TicketNotFoundException {
        logger.info("ini PUT /ticket/" + ticketId);
        ticketService.modifyTicket(ticket, ticketId);
        logger.info("end PUT /ticket/" + ticketId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/ticket/{ticketId}")
    public ResponseEntity<Void> patchTicket(@PathVariable long ticketId, @RequestBody TicketPatchDto ticketPatchDto) throws TicketNotFoundException {
        logger.info("ini PATCH /ticket/" + ticketId);
        ticketService.patchTicket(ticketId, ticketPatchDto);
        logger.info("end PATCH /ticket/" + ticketId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> TicketNotFoundException(TicketNotFoundException pnfe) {
        logger.error("Ticket not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Ticket validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

