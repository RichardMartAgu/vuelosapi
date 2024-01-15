package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.dto.FlightOutDto;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.FlightNotFoundException;
import com.svalero.vuelosapi.service.FlightService;
import jakarta.validation.Valid;
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

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                               @RequestParam(required = false) LocalDate departureDate,
                                               @RequestParam(defaultValue = "0") int gate) {

        List<Flight> flights = flightService.findAll();

        if (!name.isEmpty()) {
            flights = flights.stream()
                    .filter(flight -> flight.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (departureDate != null) {
            flights = flights.stream()
                    .filter(flight -> flight.getDepartureDate().isEqual(departureDate))
                    .collect(Collectors.toList());
        }
        if (gate > 0) {
            flights = flights.stream()
                    .filter(flight -> flight.getGate() == gate)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/airport/{airportId}/flights")
    public ResponseEntity<List<Flight>> getFlightsByAirportId(@PathVariable long airportId) {
        try {
            List<Flight> flights = flightService.findFlightsByAirportId(airportId);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } catch (AirportNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Airport not found with ID: " + airportId, e);
        }
    }


    @GetMapping("/flight/{flightId}")
    public ResponseEntity<Flight> getFlight(@PathVariable long flightId) throws FlightNotFoundException {
        Optional<Flight> optionalFlight = flightService.findById(flightId);
        Flight flight = optionalFlight.orElseThrow(() -> new FlightNotFoundException(flightId));
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<FlightOutDto> saveFlight(@Valid @RequestBody Flight flight) {
        FlightOutDto newFlight = flightService.saveFlight(flight);
        return new ResponseEntity<>(newFlight, HttpStatus.CREATED);
    }

    @DeleteMapping("/flight/{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable long flightId) throws FlightNotFoundException {
        flightService.removeFlight(flightId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/flight/{flightId}")
    public ResponseEntity<Void> modifyFlight(@Valid @RequestBody Flight flight, @PathVariable long flightId) throws FlightNotFoundException {
        flightService.modifyFlight(flight, flightId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> flightNotFoundException(FlightNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

