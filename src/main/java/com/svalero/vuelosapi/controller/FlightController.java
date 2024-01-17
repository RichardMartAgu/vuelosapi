package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.dto.FlightOutDto;
import com.svalero.vuelosapi.dto.FlightPatchDto;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.FlightNotFoundException;
import com.svalero.vuelosapi.service.FlightService;
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
public class FlightController {
    @Autowired
    private FlightService flightService;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                               @RequestParam(required = false) LocalDate departureDate,
                                               @RequestParam(defaultValue = "0") int gate) {
        logger.info("ini GET /flights by parameters: name={}, departureDate={}, gate={}", name, departureDate, gate);
        List<Flight> flightList = flightService.findAll();

        if (!name.isEmpty()) {
            flightList = flightList.stream()
                    .filter(flight -> flight.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (departureDate != null) {
            flightList = flightList.stream()
                    .filter(flight -> flight.getDepartureDate().isEqual(departureDate))
                    .collect(Collectors.toList());
        }
        if (gate > 0) {
            flightList = flightList.stream()
                    .filter(flight -> flight.getGate() == gate)
                    .collect(Collectors.toList());
        }
        logger.info("end GET /flights . List size: {}", flightList.size());
        return new ResponseEntity<>(flightList, HttpStatus.OK);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Flight> getFlight(@PathVariable long flightId) throws FlightNotFoundException {
        logger.info("ini GET/flight/" + flightId );
        Optional<Flight> optionalFlight = flightService.findById(flightId);
        Flight flight = optionalFlight.orElseThrow(() -> new FlightNotFoundException(flightId));
        logger.info("end GET/flight/" + flightId );
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @GetMapping("/airport/{airportId}/flights")
    public ResponseEntity<List<Flight>> getFlightsByAirportId(@PathVariable long airportId) {
        logger.info("ini GET/airport/ " + airportId + "/flights" );
        try {
            List<Flight> flights = flightService.findFlightsByAirportId(airportId);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } catch (AirportNotFoundException e) {
            logger.warn("AirportNotFoundException ID: " + airportId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Airport not found with ID: " + airportId, e);
        }finally {
            logger.info("end GET/airport/ " + airportId + "/flights");
        }
    }

    @PostMapping("/flights")
    public ResponseEntity<FlightOutDto> saveFlight(@Valid @RequestBody Flight flight) {
        logger.info("ini Post /flights" + flight );
        FlightOutDto newFlight = flightService.saveFlight(flight);
        logger.info("end Post /flights CREATED: {}", newFlight);
        return new ResponseEntity<>(newFlight, HttpStatus.CREATED);
    }

    @DeleteMapping("/flight/{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable long flightId) throws FlightNotFoundException {
        logger.info("ini DELETE /flight/" + flightId );
        flightService.removeFlight(flightId);
        logger.info("end DELETE /flight/" + flightId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/flight/{flightId}")
    public ResponseEntity<Void> modifyFlight(@Valid @RequestBody Flight flight, @PathVariable long flightId) throws FlightNotFoundException {
        logger.info("ini PUT /flight/" + flightId );
        flightService.modifyFlight(flight, flightId);
        logger.info("end PUT /flight/" + flightId );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/flight/{flightId}")
    public ResponseEntity<Void> patchFlight(@PathVariable long flightId, @RequestBody FlightPatchDto flightPatchDto) throws FlightNotFoundException {
        logger.info("ini PATCH /flight/" + flightId );
        flightService.patchFlight(flightId, flightPatchDto);
        logger.info("end PATCH /flight/" + flightId );
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> flightNotFoundException(FlightNotFoundException pnfe) {
        logger.error("Flight not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Flight validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

