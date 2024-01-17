package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.AirlineController;
import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.dto.AirlinePatchDto;
import com.svalero.vuelosapi.dto.AirportPatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.repository.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);


    public List<Airport> findAll() {
        logger.info("Do Airport findAll");
        return airportRepository.findAll();
    }

    public Optional<Airport> findById(long id) {
        logger.info("Do Airport findById " + id);
        return airportRepository.findById(id);
    }

    public Airport saveAirport(Airport airport) {
        logger.info("Ini saveAirport " + airport);
        airportRepository.save(airport);
        logger.info("End saveAirport " + airport);
        return airport;
    }

    public void removeAirport(long airportId) throws AirportNotFoundException {
        logger.info("Ini removeAirport ID: " + airportId);
        Airport airport = airportRepository.findById(airportId).orElseThrow(() -> new AirportNotFoundException(airportId));
        logger.info("End removeAirport Airport: " + airportId);
        airportRepository.delete(airport);
    }

    public void modifyAirport(Airport newAirport, long airportId) throws AirportNotFoundException {
        logger.info("Ini modifyAirport ID: " + airportId);
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

        logger.info("End modifyAirport Airport: " + airport);
    }

    public void patchAirport(long airportId, AirportPatchDto airportPatchDto) throws AirportNotFoundException {
        logger.info("Ini patchAirport ID: " + airportId);
        Airport oldAirport = airportRepository.findById(airportId).orElseThrow(AirportNotFoundException::new);
        if (airportPatchDto.getField().equals("name")) {
            oldAirport.setName(airportPatchDto.getName());
        }
        airportRepository.save((oldAirport));
        logger.info("End patchAirport");
    }
}