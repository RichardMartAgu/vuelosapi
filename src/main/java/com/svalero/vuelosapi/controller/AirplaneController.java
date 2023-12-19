package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Airplane;
import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.service.AirplaneService;
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
public class AirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @GetMapping("/airplanes")
    public ResponseEntity<List<Airplane>> getAll(@Valid @RequestParam(defaultValue = "") String model,
                                                @RequestParam(defaultValue = "0") int passengerCapacity,
                                                @RequestParam(required = false) LocalDate manufacturingDate) {

        List<Airplane> airplaneList = airplaneService.findAll();

        if (!model.isEmpty()) {
            airplaneList = airplaneList.stream()
                    .filter(airport -> airport.getModel().contains(model))
                    .collect(Collectors.toList());
        }
        if (passengerCapacity > 0) {
            airplaneList = airplaneList.stream()
                    .filter(airport -> airport.getPassengerCapacity() ==passengerCapacity)
                    .collect(Collectors.toList());
        }
        if (manufacturingDate != null) {
            airplaneList = airplaneList.stream()
                    .filter(flight -> flight.getManufacturingDate().isEqual(manufacturingDate))
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(airplaneList, HttpStatus.OK);
    }


    @GetMapping("/airplane/{airplaneId}")
    public ResponseEntity<Airplane> getAirplane(@PathVariable long airplaneId) throws AirplaneNotFoundException {
        Optional<Airplane> optionalAirplane = airplaneService.findById(airplaneId);
        Airplane airplane = optionalAirplane.orElseThrow(() -> new AirplaneNotFoundException(airplaneId));
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @PostMapping("/airplanes")
    public ResponseEntity<Void> saveAirplane(@Valid@RequestBody Airplane airplane) {
        airplaneService.saveAirplane(airplane);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/airplane/{airplaneId}")
    public ResponseEntity<Void> daleteAirplane(@PathVariable long airplaneId) throws AirplaneNotFoundException {
        airplaneService.removeAirplane(airplaneId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/airplane/{airplaneId}")
    public ResponseEntity<Void> modifyAirplane(@Valid@RequestBody Airplane airplane, @PathVariable long airplaneId) throws AirplaneNotFoundException{
        airplaneService.modifyAirplane(airplane, airplaneId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler(AirplaneNotFoundException.class)
    public ResponseEntity<ErrorResponse> AirplaneNotFoundException(AirplaneNotFoundException pnfe) {
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

