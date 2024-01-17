package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.AirportStore;
import com.svalero.vuelosapi.dto.AirportStoreOutDto;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.dto.AirportStorePatchDto;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportStoreNotFoundException;
import com.svalero.vuelosapi.service.AirportStoreService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AirportStoreController {
    @Autowired
    private AirportStoreService airportStoreService;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    @GetMapping("/airportStores")
    public ResponseEntity<List<AirportStore>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                     @RequestParam(defaultValue = "") String type,
                                                     @RequestParam(defaultValue = "0") float averageProfit) {
        logger.info("ini GET /airportStores by parameters: name={}, type={}, averageProfit={}", name, type, averageProfit);
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
        logger.info("end GET /airportStores . List size: {}", airportStoreList.size());
        return new ResponseEntity<>(airportStoreList, HttpStatus.OK);
    }


    @GetMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<AirportStore> getAirportStore(@PathVariable long airportStoreId) throws AirportStoreNotFoundException {
        logger.info("ini GET/airportStore/" + airportStoreId);
        Optional<AirportStore> optionalAirportStore = airportStoreService.findById(airportStoreId);
        AirportStore airportStore = optionalAirportStore.orElseThrow(() -> new AirportStoreNotFoundException(airportStoreId));
        logger.info("end GET/airportStore/" + airportStoreId);
        return new ResponseEntity<>(airportStore, HttpStatus.OK);
    }

    @GetMapping("/airport/{airportId}/airportStores")
    public ResponseEntity<List<AirportStore>> getStoreByAirportId(@PathVariable long airportId) {
        logger.info("ini GET/airport/ " + airportId + "/airportStores");
        try {
            List<AirportStore> airportStore = airportStoreService.findStoreByAirportId(airportId);
            return new ResponseEntity<>(airportStore, HttpStatus.OK);
        } catch (AirportNotFoundException e) {
            logger.warn("AirportNotFoundException ID: " + airportId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Airport not found with ID: " + airportId, e);
        } finally {
            logger.info("end GET/airport/ " + airportId + "/airportStores");
        }
    }

    @PostMapping("/airportStores")
    public ResponseEntity<AirportStoreOutDto> saveAirportStore(@Valid @RequestBody AirportStore airportStore) {
        logger.info("ini Post /airportStores" + airportStore);
        AirportStoreOutDto newAirportStore = airportStoreService.saveAirportStore(airportStore);
        logger.info("end Post /airportStores CREATED: {}", newAirportStore);
        return new ResponseEntity<>(newAirportStore, HttpStatus.CREATED);
    }


    @DeleteMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<Void> deleteAirportStore(@PathVariable long airportStoreId) throws AirportStoreNotFoundException {
        logger.info("ini DELETE /airportStore/" + airportStoreId);
        airportStoreService.removeAirportStore(airportStoreId);
        logger.info("end DELETE /airportStore/" + airportStoreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airportStore/{airportStoreId}")
    public ResponseEntity<Void> modifyAirportStore(@Valid @RequestBody AirportStore airportStore, @PathVariable long airportStoreId) throws AirportStoreNotFoundException {
        logger.info("ini PUT /airportStore/" + airportStoreId );
        airportStoreService.modifyAirportStore(airportStore, airportStoreId);
        logger.info("end PUT /airportStore/" + airportStoreId );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value = "/airportStore/{airportStoreId}")
    public ResponseEntity<Void> patchAirportStore(@PathVariable long airportStoreId, @RequestBody AirportStorePatchDto airportStorePatchDto) throws AirportStoreNotFoundException {
        logger.info("ini PATCH /airportStore/" + airportStoreId );
        airportStoreService.patchAirportStore(airportStoreId, airportStorePatchDto);
        logger.info("end PATCH /airportStore/" + airportStoreId );
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AirportStoreNotFoundException.class)
    public ResponseEntity<ErrorResponse> airportStoreNotFoundException(AirportStoreNotFoundException pnfe) {
        logger.error("AirportStore not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("AirportStore validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

