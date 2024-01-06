package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;


    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }


    public Optional<Airline> findById(long id) {
        return airlineRepository.findById(id);
    }


    public void saveAirline(Airline airline) {
        airlineRepository.save(airline);
    }

    public void removeAirline(long airlineId) throws AirlineNotFoundException {
        Airline airline = airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(airlineId));
        airlineRepository.delete(airline);
    }

    public void modifyAirline(Airline newAirline, long airlineId) throws AirlineNotFoundException {
        Optional<Airline> airline = airlineRepository.findById(airlineId);
        if (airline.isPresent()) {
            Airline existingAirline = airline.get();
            existingAirline.setName(newAirline.getName());
            existingAirline.setTelephone(newAirline.getTelephone());
            existingAirline.setFoundationYear(newAirline.getFoundationYear());
            existingAirline.setFleet(newAirline.getFleet());
            existingAirline.setOnTime(newAirline.getOnTime());
            existingAirline.setActive(newAirline.isActive());


            airlineRepository.save(existingAirline);
        } else {
            throw new AirlineNotFoundException(airlineId);

        }

    }
}