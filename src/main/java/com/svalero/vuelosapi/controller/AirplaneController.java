package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airplane;
import com.svalero.vuelosapi.dto.AirplaneOutDto;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.dto.AirplanePatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.service.AirlineService;
import com.svalero.vuelosapi.service.AirplaneService;
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
public class AirplaneController {
    @Autowired
    private AirplaneService airplaneService;
    @Autowired
    private AirlineService airlineService;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    @GetMapping("/airplanes")
    public ResponseEntity<List<Airplane>> getAll(@Valid @RequestParam(defaultValue = "") String model,
                                                 @RequestParam(defaultValue = "0") int passengerCapacity,
                                                 @RequestParam(required = false) LocalDate manufacturingDate) {
        logger.info("ini GET /airplanes by parameters: model={}, passengerCapacity={}, manufacturingDate={}", model, passengerCapacity, manufacturingDate);
        List<Airplane> airplaneList = airplaneService.findAll();

        if (!model.isEmpty()) {
            airplaneList = airplaneList.stream()
                    .filter(airplane -> airplane.getModel().contains(model))
                    .collect(Collectors.toList());
        }
        if (passengerCapacity > 0) {
            airplaneList = airplaneList.stream()
                    .filter(airplane -> airplane.getPassengerCapacity() == passengerCapacity)
                    .collect(Collectors.toList());
        }
        if (manufacturingDate != null) {
            airplaneList = airplaneList.stream()
                    .filter(airplane -> airplane.getManufacturingDate().isEqual(manufacturingDate))
                    .collect(Collectors.toList());
        }
        logger.info("end GET /airplanes . List size: {}", airplaneList.size());
        return new ResponseEntity<>(airplaneList, HttpStatus.OK);
    }

    @GetMapping("/airplane/{airplaneId}")
    public ResponseEntity<Airplane> getAirplane(@PathVariable long airplaneId) throws AirplaneNotFoundException {
        logger.info("ini GET/airplane/" + airplaneId );
        Optional<Airplane> optionalAirplane = airplaneService.findById(airplaneId);
        Airplane airplane = optionalAirplane.orElseThrow(() -> new AirplaneNotFoundException(airplaneId));
        logger.info("end GET/airplane/" + airplaneId );
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @GetMapping("/airline/{airlineId}/airplanes")
    public ResponseEntity<List<Airplane>> getAirplaneByAirlineId(@PathVariable long airlineId) {
        logger.info("ini GET/airline/ " + airlineId + "/airplanes" );
        try {
            List<Airplane> airplane = airplaneService.findAirplaneByAirlineId(airlineId);
            return new ResponseEntity<>(airplane, HttpStatus.OK);
        } catch (AirlineNotFoundException e) {
            logger.warn("AirlineNotFoundException ID: " + airlineId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Airline not found with ID: " + airlineId, e);
        }finally {
            logger.info("end GET/airline/ " + airlineId + "/airplanes");
        }
    }

    @PostMapping("/airplane")
    public ResponseEntity<AirplaneOutDto> saveAirplane(@Valid @RequestBody Airplane airplane) {
        logger.info("ini Post /airplane" + airplane );
        AirplaneOutDto newAirplane = airplaneService.saveAirplane(airplane);
        logger.info("end Post /airplane CREATED: {}", newAirplane);
        return new ResponseEntity<>(newAirplane, HttpStatus.CREATED);
    }


    @DeleteMapping("/airplane/{airplaneId}")
    public ResponseEntity<Void> deleteAirplane(@PathVariable long airplaneId) throws AirplaneNotFoundException {
        logger.info("ini DELETE /airplane/" + airplaneId );
        airplaneService.removeAirplane(airplaneId);
        logger.info("end DELETE /airplane/" + airplaneId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airplane/{airplaneId}")
    public ResponseEntity<Void> modifyAirplane(@Valid @RequestBody Airplane airplane, @PathVariable long airplaneId) throws AirplaneNotFoundException {
        logger.info("ini PUT /airplane/" + airplaneId );
        airplaneService.modifyAirplane(airplane, airplaneId);
        logger.info("end PUT /airplane/" + airplaneId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping(value = "/airplane/{airplaneId}")
    public ResponseEntity<Void> patchAirplane(@PathVariable long airplaneId, @RequestBody AirplanePatchDto airplanePatchDto) throws AirplaneNotFoundException {
        logger.info("ini PATCH /airplane/" + airplaneId );
        airplaneService.patchAirplane(airplaneId, airplanePatchDto);
        logger.info("end PATCH /airplane/" + airplaneId );
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AirplaneNotFoundException.class)
    public ResponseEntity<ErrorResponse> AirplaneNotFoundException(AirplaneNotFoundException pnfe) {
        logger.error("Airplane not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Airplane validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

