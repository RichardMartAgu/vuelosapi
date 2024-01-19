package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.dto.AirlineDto;
import com.svalero.vuelosapi.dto.AirlinePatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.service.AirlineService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    @GetMapping("/airlines")
    public ResponseEntity<List<Airline>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "0") int telephone,
                                                @RequestParam(defaultValue = "0") int fleet) {
        logger.info("ini GET /airlines by parameters: name={}, telephone={}, fleet={}", name, telephone, fleet);
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
        logger.info("end GET /airlines . List size: {}", airlinesList.size());
        return new ResponseEntity<>(airlinesList, HttpStatus.OK);
    }

    @GetMapping("/airline/{airlineId}")
    public ResponseEntity<Airline> getAirline(@PathVariable long airlineId) throws AirlineNotFoundException {
        logger.info("ini GET/airline/" + airlineId );
        Optional<Airline> optionalAirline = airlineService.findById(airlineId);
        Airline airline = optionalAirline.orElseThrow(() -> new AirlineNotFoundException(airlineId));
        logger.info("end GET/airline/" + airlineId );
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }

    @PostMapping("/airline")
    public ResponseEntity<Airline> saveAirline(@Valid @RequestBody Airline airline) {
        logger.info("ini Post /airline" + airline );
        Airline newAirline = airlineService.saveAirline(airline);
        logger.info("end Post /airline CREATED: {}", newAirline);
        return new ResponseEntity<>(newAirline, HttpStatus.CREATED);
    }


    @DeleteMapping("/airline/{airlineId}")
    public ResponseEntity<Void> deleteAirline(@PathVariable long airlineId) throws AirlineNotFoundException {
        logger.info("ini DELETE /airline/" + airlineId );
        airlineService.removeAirline(airlineId);
        logger.info("end DELETE /airline/" + airlineId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airline/{airlineId}")
    public ResponseEntity<Void> modifyAirline(@Valid @RequestBody AirlineDto airlineDto, @PathVariable long airlineId) throws AirlineNotFoundException {
        logger.info("ini PUT /airline/" + airlineId );
        airlineService.modifyAirline(airlineDto, airlineId);
        logger.info("end PUT /airline/" + airlineId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping(value = "/airline/{airlineId}")
    public ResponseEntity<Void> patchAirline(@PathVariable long airlineId, @RequestBody AirlinePatchDto airlinePatchDto) throws AirlineNotFoundException {
        logger.info("ini PATCH /airline/" + airlineId );
        airlineService.patchAirline(airlineId, airlinePatchDto);
        logger.info("end PATCH /airline/" + airlineId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<ErrorResponse> airlineNotFoundException(AirlineNotFoundException pnfe) {
        logger.error("Airline not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Airline validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

