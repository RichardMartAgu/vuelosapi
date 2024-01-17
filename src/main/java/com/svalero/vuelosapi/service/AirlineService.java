package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.AirlineController;
import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.dto.AirlinePatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.repository.AirlineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);


    public List<Airline> findAll() {
        logger.info("Do Airline findAll");
        return airlineRepository.findAll();
    }


    public Optional<Airline> findById(long id) {
        logger.info("Do Airline findById " + id);
        return airlineRepository.findById(id);
    }


    public Airline saveAirline(Airline airline) {
        logger.info("Ini saveAirline " + airline);
        airlineRepository.save(airline);
        logger.info("End saveAirline " + airline);
        return airline;
    }

    public void removeAirline(long airlineId) throws AirlineNotFoundException {
        logger.info("Ini removeAirline ID: " + airlineId);
        Airline airline = airlineRepository.findById(airlineId).orElseThrow(() -> new AirlineNotFoundException(airlineId));
        logger.info("End removeAirline Airline: " + airline);
        airlineRepository.delete(airline);
    }

    public void modifyAirline(Airline newAirline, long airlineId) throws AirlineNotFoundException {
        logger.info("Ini modifyAirline ID: " + airlineId);
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
        logger.info("End modifyAirline Airline: " + airline);
    }

    public void patchAirline(long airlineId, AirlinePatchDto airlinePatchDto) throws AirlineNotFoundException {
        logger.info("Ini patchAirline ID: " + airlineId);
        Airline oldAirline = airlineRepository.findById(airlineId).orElseThrow(AirlineNotFoundException::new);
        if (airlinePatchDto.getField().equals("fleet")) {
            oldAirline.setFleet(airlinePatchDto.getFleet());
        }
        airlineRepository.save((oldAirline));
        logger.info("End patchAirline");
    }

}