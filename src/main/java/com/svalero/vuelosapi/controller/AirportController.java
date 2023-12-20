package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AirportController {
    @Autowired
    private AirportService airportService;

    @GetMapping("/airports")
    public ResponseEntity<List<Airport>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "") String city,
                                                @RequestParam(required = false) LocalDate foundationYear) {

        List<Airport> airportList = airportService.findAll();

        if (!name.isEmpty()) {
            airportList = airportList.stream()
                    .filter(airport -> airport.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (!city.isEmpty()) {
            airportList = airportList.stream()
                    .filter(airport -> airport.getCity().contains(city))
                    .collect(Collectors.toList());
        }
        if (foundationYear != null) {
            airportList = airportList.stream()
                    .filter(airport -> airport.getFoundationYear().isEqual(foundationYear))
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(airportList, HttpStatus.OK);
    }


    @GetMapping("/airport/{airportId}")
    public ResponseEntity<Airport> getAirport(@PathVariable long airportId) throws AirportNotFoundException {
        Optional<Airport> optionalAirport = airportService.findById(airportId);
        Airport airport = optionalAirport.orElseThrow(() -> new AirportNotFoundException(airportId));
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @PostMapping("/airports")
    public ResponseEntity<Void> saveAirport(@Valid@RequestBody Airport airport) {
        airportService.saveAirport(airport);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/airport/{airportId}")
    public ResponseEntity<Void> daleteAirport(@PathVariable long airportId) throws AirportNotFoundException {
        airportService.removeAirport(airportId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airport/{airportId}")
    public ResponseEntity<Void> modifyAirport(@Valid@RequestBody Airport airport, @PathVariable long airportId) throws AirportNotFoundException{
        airportService.modifyAirport(airport, airportId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<ErrorResponse> airportNotFoundException(AirportNotFoundException pnfe) {
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

