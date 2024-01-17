package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.AirlineController;
import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.dto.FlightOutDto;
import com.svalero.vuelosapi.dto.FlightPatchDto;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.FlightNotFoundException;
import com.svalero.vuelosapi.repository.FlightRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);


    public List<Flight> findAll() {
        logger.info("Do Flight findAll");
        return flightRepository.findAll();
    }

    public Optional<Flight> findById(long id) {
        logger.info("Do Flight findById " + id);
        return flightRepository.findById(id);
    }

    public List<Flight> findFlightsByAirportId(long airportId) throws AirportNotFoundException {
        logger.info("Ini findFlightsByAirportId " + airportId);
        Optional<Airport> airportOptional = airportService.findById(airportId);
        if (airportOptional.isPresent()) {
            logger.info("End findFlightsByAirportId " + airportId);
            return flightRepository.findFlightByAirport(airportOptional);
        } else {
            throw new AirportNotFoundException();
        }
    }

    public FlightOutDto saveFlight(Flight flight) {
        logger.info("Ini saveFlight " + flight);
        Flight newflight = flightRepository.save(flight);
        FlightOutDto flightOutDto = new FlightOutDto();
        modelMapper.map(newflight, flightOutDto);
        logger.info("End saveFlight " + flight);
        return flightOutDto;
    }

    public void removeFlight(long flightId) throws FlightNotFoundException {
        logger.info("Ini removeFlight ID: " + flightId);
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));
        logger.info("End removeFlight Airplane: " + flightId);
        flightRepository.delete(flight);
    }

    public void modifyFlight(Flight newflight, long flightId) throws FlightNotFoundException {
        logger.info("Ini modifyFlight ID: " + flightId);
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
        logger.info("End modifyFlight Flight: " + flight);
    }

    public void patchFlight(long flightId, FlightPatchDto flightPatchDto) throws FlightNotFoundException {
        logger.info("Ini patchFlight ID: " + flightId);
        Flight oldFlight = flightRepository.findById(flightId).orElseThrow(FlightNotFoundException::new);
        if (flightPatchDto.getField().equals("gate")) {
            oldFlight.setGate(flightPatchDto.getGate());
        }
        flightRepository.save((oldFlight));
        logger.info("End patchFlight");
    }
}