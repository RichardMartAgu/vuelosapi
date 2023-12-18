package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;


    public List<Flight> findAll() {
        return flightRepository.findAll();
    }
    public Optional<Flight> findById(long id) {
        return flightRepository.findById(id);
    }

    public void saveFlight (Flight flight){
        flightRepository.save(flight);
    }

    public void removeFlight ( long flightId){
        flightRepository.deleteById(flightId);
    }

    public void modifyFlight (Flight newflight, long flightId){
        Optional<Flight> flight = flightRepository.findById(flightId);
        if (flight.isPresent()){
            Flight existingFligh = flight.get();
            existingFligh.setName(newflight.getName());
            existingFligh.setDepartureDate(newflight.getDepartureDate());
            existingFligh.setGate(newflight.getGate());
            existingFligh.setDuration(newflight.getDuration());
            existingFligh.setInternational(newflight.isInternational());

            flightRepository.save(existingFligh);
        }

    }
}