package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AirlineController {
    @Autowired
    private AirlineService airlineService;

    @GetMapping("/airlines")
    public ResponseEntity<List<Airline>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "0") int telephone,
                                                @RequestParam(defaultValue = "0") int fleet) {

        List<Airline> airlinesList = airlineService.findAll();

        if (!name.isEmpty()) {
            airlinesList = airlinesList.stream()
                    .filter(airline -> airline.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (telephone > 0) {
            airlinesList = airlinesList.stream()
                    .filter(airline -> airline.getTelephone() == telephone)
                    .collect(Collectors.toList());
        }
        if (fleet > 0) {
            airlinesList = airlinesList.stream()
                    .filter(airline -> airline.getFleet() == fleet)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(airlinesList, HttpStatus.OK);
    }

    @GetMapping("/airline/{airlineId}")
    public ResponseEntity<Airline> getAirline(@PathVariable long airlineId) throws AirlineNotFoundException {
        Optional<Airline> optionalAirline = airlineService.findById(airlineId);
        Airline flight = optionalAirline.orElseThrow(() -> new AirlineNotFoundException(airlineId));
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @PostMapping("/airline")
    public ResponseEntity<Airline> saveAirline(@Valid @RequestBody Airline airline) {
        Airline newAirline = airlineService.saveAirline(airline);
        return new ResponseEntity<>(newAirline, HttpStatus.CREATED);
    }

    @DeleteMapping("/airline/{airlineId}")
    public ResponseEntity<Void> deleteAirline(@PathVariable long airlineId) throws AirlineNotFoundException {
        airlineService.removeAirline(airlineId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airline/{airlineId}")
    public ResponseEntity<Void> modifyAirline(@Valid @RequestBody Airline airline, @PathVariable long airlineId) throws AirlineNotFoundException {
        airlineService.modifyAirline(airline, airlineId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<ErrorResponse> airlineNotFoundException(AirlineNotFoundException pnfe) {
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

