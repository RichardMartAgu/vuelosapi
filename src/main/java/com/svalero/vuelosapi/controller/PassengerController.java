package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.domain.Passenger;
import com.svalero.vuelosapi.dto.PassengerPatchDto;
import com.svalero.vuelosapi.exceptions.PassengerNotFoundException;
import com.svalero.vuelosapi.service.PassengerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PassengerController {

    @Autowired
    private PassengerService passengerService;
    private Logger logger = LoggerFactory.getLogger(com.svalero.vuelosapi.controller.PassengerController.class);

    @GetMapping("/passenger")
    public ResponseEntity<List<Passenger>> getAll(@Valid @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "0") String surname, @RequestParam(defaultValue = "0") String dni) {
        logger.info("ini GET /passenger by parameters: name={}, surname={}, dni={}", name, surname, dni);

        List<Passenger> passengerList = passengerService.findAll();

        if (!name.isEmpty()) {
            passengerList = passengerList.stream().filter(passenger -> passenger.getName().contains(name)).collect(Collectors.toList());
        }
        if (!surname.isEmpty()) {
            passengerList = passengerList.stream().filter(passenger -> passenger.getSurname().contains(surname)).collect(Collectors.toList());
        }
        if (!dni.isEmpty()) {
            passengerList = passengerList.stream().filter(passenger -> Objects.equals(passenger.getDni(), dni)).collect(Collectors.toList());
        }
        logger.info("end GET /passenger . List size: {}", passengerList.size());
        return new ResponseEntity<>(passengerList, HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable long passengerId) throws PassengerNotFoundException {
        logger.info("ini GET/passenger/" + passengerId);
        Optional<Passenger> optionalPassenger = passengerService.findById(passengerId);
        Passenger passenger = optionalPassenger.orElseThrow(() -> new PassengerNotFoundException(passengerId));
        logger.info("end GET/passenger/" + passengerId);
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    @PostMapping("/passenger")
    public ResponseEntity<Passenger> savePassenger(@Valid @RequestBody Passenger passenger) {
        logger.info("ini Post /passenger" + passenger);
        Passenger newPassenger = passengerService.savePassenger(passenger);
        logger.info("end Post /passenger CREATED: {}", newPassenger);
        return new ResponseEntity<>(newPassenger, HttpStatus.CREATED);
    }


    @DeleteMapping("/passenger/{passengerId}")
    public ResponseEntity<Void> deletePassenger(@PathVariable long passengerId) throws PassengerNotFoundException {
        logger.info("ini DELETE /passenger/" + passengerId);
        passengerService.removePassenger(passengerId);
        logger.info("end DELETE /passenger/" + passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/passenger/{passengerId}")
    public ResponseEntity<Void> modifyPassenger(@Valid @RequestBody Passenger passenger, @PathVariable long passengerId) throws PassengerNotFoundException {
        logger.info("ini PUT /passenger/" + passengerId);
        passengerService.modifyPassenger(passenger, passengerId);
        logger.info("end PUT /passenger/" + passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/passenger/{passengerId}")
    public ResponseEntity<Void> patchPassenger(@PathVariable long passengerId, @RequestBody PassengerPatchDto passengerPatchDto) throws PassengerNotFoundException {
        logger.info("ini PATCH /passenger/" + passengerId);
        passengerService.patchPassenger(passengerId, passengerPatchDto);
        logger.info("end PATCH /passenger/" + passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ErrorResponse> PassengerNotFoundException(PassengerNotFoundException pnfe) {
        logger.error("Passenger not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Passenger validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

