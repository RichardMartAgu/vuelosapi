package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.ErrorResponse;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.exceptions.FlightNotFoundException;
import com.svalero.vuelosapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FlightController {
  @Autowired private FlightService flightService;

  @GetMapping("/flights")
  public ResponseEntity <List<Flight>> getAll(@RequestParam(defaultValue = "") String name,
                                              @RequestParam(required = false)LocalDate departureDate,
                                              @RequestParam (defaultValue = "0" ) int gate) {

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
  @GetMapping("/flight/{flightId}")
  public ResponseEntity<Flight> getFlight(@PathVariable long flightId) throws FlightNotFoundException {
    Optional<Flight> optionalFlight = flightService.findById(flightId);
    Flight flight = optionalFlight.orElseThrow(() -> new FlightNotFoundException(flightId));
    return new ResponseEntity<>(flight, HttpStatus.OK);
  }

  @PostMapping("/flights")
  public ResponseEntity<Void> saveFlight(@RequestBody Flight flight) {
    flightService.saveFlight(flight);
    return  new ResponseEntity<>( HttpStatus.CREATED);
  }

  @DeleteMapping("/flight/{flightId}")
    public ResponseEntity<Void>  daleteFlight (@PathVariable long flightId) throws Exception{
      flightService.removeFlight(flightId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  @PutMapping ("/flight/{flightId}")
    public ResponseEntity<Void> modifyFlight(@RequestBody Flight flight, @PathVariable long flightId){
      flightService.modifyFlight(flight, flightId);
      return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @ExceptionHandler(FlightNotFoundException.class)
  public ResponseEntity<ErrorResponse> productNotFoundException(FlightNotFoundException pnfe) {
    ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }
}

