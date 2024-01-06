package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.FlightNotFoundException;
import com.svalero.vuelosapi.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportService airportService;


    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> findFlightsByAirportId(long airportId) throws AirportNotFoundException {
        Optional<Airport> airportOptional = airportService.findById(airportId);
        if (airportOptional.isPresent()) {
            return flightRepository.findFlightByAirport(airportOptional);
        } else {
            throw new AirportNotFoundException();
        }
    }

    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    public void removeFlight(long flightId) throws FlightNotFoundException {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));
        flightRepository.delete(flight);
    }

    public void modifyFlight(Flight newflight, long flightId) throws FlightNotFoundException {
        Optional<Flight> flight = flightRepository.findById(flightId);
        if (flight.isPresent()) {
            Flight existingFligh = flight.get();
            existingFligh.setName(newflight.getName());
            existingFligh.setDepartureDate(newflight.getDepartureDate());
            existingFligh.setGate(newflight.getGate());
            existingFligh.setDuration(newflight.getDuration());
            existingFligh.setDeparture(newflight.isDeparture());
            existingFligh.setAirport(newflight.getAirport());

            flightRepository.save(existingFligh);
        } else {
            throw new FlightNotFoundException(flightId);

        }

    }
}