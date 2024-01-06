package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.AirportStore;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportStoreNotFoundException;
import com.svalero.vuelosapi.service.AirportStoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AirportStoreController {
    @Autowired
    private AirportStoreService airportStoreService;

    @GetMapping("/airportStores")
    public ResponseEntity<List<AirportStore>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                     @RequestParam(defaultValue = "") String type,
                                                     @RequestParam(defaultValue = "0") float averageProfit) {

        List<AirportStore> airportStoreList = airportStoreService.findAll();

        if (!name.isEmpty()) {
            airportStoreList = airportStoreList.stream()
                    .filter(airportStore -> airportStore.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (!type.isEmpty()) {
            airportStoreList = airportStoreList.stream()
                    .filter(airportStore -> airportStore.getType().contains(type))
                    .collect(Collectors.toList());
        }
        if (averageProfit > 0) {
            airportStoreList = airportStoreList.stream()
                    .filter(airportStore -> airportStore.getAverageProfit() == (averageProfit))
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(airportStoreList, HttpStatus.OK);
    }


    @GetMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<AirportStore> getAirportStore(@PathVariable long airportStoreId) throws AirportStoreNotFoundException {
        Optional<AirportStore> optionalAirportStore = airportStoreService.findById(airportStoreId);
        AirportStore airportStore = optionalAirportStore.orElseThrow(() -> new AirportStoreNotFoundException(airportStoreId));
        return new ResponseEntity<>(airportStore, HttpStatus.OK);
    }

    @GetMapping("/airport/{airportId}/airportStores")
    public ResponseEntity<List<AirportStore>> getStoreByAirportId(@PathVariable long airportId) {
        try {
            List<AirportStore> airportStore = airportStoreService.findStoreByAirportId(airportId);
            return new ResponseEntity<>(airportStore, HttpStatus.OK);
        } catch (AirportNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Airport not found with ID: " + airportId, e);
        }
    }

    @PostMapping("/airportStores")
    public ResponseEntity<Void> saveAirportStore(@Valid @RequestBody AirportStore airportStore) {
        airportStoreService.saveAirportStore(airportStore);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<Void> daleteAirportStore(@PathVariable long airportStoreId) throws AirportStoreNotFoundException {
        airportStoreService.removeAirportStore(airportStoreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<Void> modifyAirportStore(@Valid @RequestBody AirportStore airportStore, @PathVariable long airportStoreId)
            throws AirportStoreNotFoundException {
        airportStoreService.modifyAirportStore(airportStore, airportStoreId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(AirportStoreNotFoundException.class)
    public ResponseEntity<ErrorResponse> airportStoreNotFoundException(AirportStoreNotFoundException pnfe) {
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

