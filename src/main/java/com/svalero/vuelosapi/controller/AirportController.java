package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.dto.AirplanePatchDto;
import com.svalero.vuelosapi.dto.AirportPatchDto;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.service.AirportService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    @GetMapping("/airports")
    public ResponseEntity<List<Airport>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "") String city,
                                                @RequestParam(required = false) LocalDate foundationYear) {
        logger.info("ini GET /airports by parameters: name={}, city={}, foundationYear={}", name, city, foundationYear);
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
        logger.info("end GET /airports . List size: {}", airportList.size());
        return new ResponseEntity<>(airportList, HttpStatus.OK);
    }


    @GetMapping("/airport/{airportId}")
    public ResponseEntity<Airport> getAirport(@PathVariable long airportId) throws AirportNotFoundException {
        logger.info("ini GET/airport/" + airportId );
        Optional<Airport> optionalAirport = airportService.findById(airportId);
        Airport airport = optionalAirport.orElseThrow(() -> new AirportNotFoundException(airportId));
        logger.info("end GET/airport/" + airportId );
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @PostMapping("/airports")
    public ResponseEntity<Airport> saveAirport(@Valid @RequestBody Airport airport) {
        logger.info("ini Post /airports" + airport );
        Airport newAirport = airportService.saveAirport(airport);
        logger.info("end Post /airports CREATED: {}", newAirport);
        return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
    }


    @DeleteMapping("/airport/{airportId}")
    public ResponseEntity<Void> deleteAirport(@PathVariable long airportId) throws AirportNotFoundException {
        logger.info("ini DELETE /airport/" + airportId );
        airportService.removeAirport(airportId);
        logger.info("end DELETE /airport/" + airportId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airport/{airportId}")
    public ResponseEntity<Void> modifyAirport(@Valid @RequestBody Airport airport, @PathVariable long airportId) throws AirportNotFoundException {
        logger.info("ini PUT /airport/" + airportId );
        airportService.modifyAirport(airport, airportId);
        logger.info("end PATCH /airport/" + airportId );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping(value = "/airport/{airportId}")
    public ResponseEntity<Void> patchAirport(@PathVariable long airportId, @RequestBody AirportPatchDto airportPatchDto) throws AirportNotFoundException {
        logger.info("ini PATCH /airport/" + airportId );
        airportService.patchAirport(airportId, airportPatchDto);
        logger.info("end PATCH /airport/" + airportId );
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<ErrorResponse> airportNotFoundException(AirportNotFoundException pnfe) {
        logger.error("Airport not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Airport validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

