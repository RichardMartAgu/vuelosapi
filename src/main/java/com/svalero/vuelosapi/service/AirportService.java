package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;


    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public Optional<Airport> findById(long id) {
        return airportRepository.findById(id);
    }

    public Airport saveAirport(Airport airport) {
        airportRepository.save(airport);
        return airport;
    }

    public void removeAirport(long airportId) throws AirportNotFoundException {
        Airport airport = airportRepository.findById(airportId).orElseThrow(() -> new AirportNotFoundException(airportId));
        airportRepository.delete(airport);
    }

    public void modifyAirport(Airport newAirport, long airportId) throws AirportNotFoundException {
        Optional<Airport> airport = airportRepository.findById(airportId);
        if (airport.isPresent()) {
            Airport existingAirport = airport.get();
            existingAirport.setName(newAirport.getName());
            existingAirport.setCity(newAirport.getCity());
            existingAirport.setFoundationYear(newAirport.getFoundationYear());
            existingAirport.setLatitude(newAirport.getLatitude());
            existingAirport.setLongitude(newAirport.getLongitude());
            existingAirport.setActive(newAirport.isActive());

            airportRepository.save(existingAirport);
        } else {
            throw new AirportNotFoundException(airportId);

        }

    }
}