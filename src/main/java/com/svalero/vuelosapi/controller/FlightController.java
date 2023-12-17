package com.svalero.vuelosapi.controller;

import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FlightController {
  @Autowired private FlightService flightService;

  @GetMapping("/flights")
  public List<Flight> getAll() {
    return flightService.findAll();
  }

  @PostMapping("/flights")
  public void saveFligh(@RequestBody Flight flight) {
    flightService.saveFlight(flight);
  }
  @DeleteMapping("/flight/{flightId}")
    public void daleteFlight (@PathVariable long flightId){
      flightService.removeFlight(flightId);
  }
  @PutMapping ("/flight/{flightId}")
    public void modifyFlight(@RequestBody Flight flight, @PathVariable long flightId){
      flightService.modifyFlight(flight, flightId);
  }
}

