package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.AirlineController;
import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.Airplane;
import com.svalero.vuelosapi.dto.AirplaneOutDto;
import com.svalero.vuelosapi.dto.AirplanePatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.repository.AirplaneRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;
    @Autowired
    private AirlineService airlineService;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);

    public List<Airplane> findAll() {
        logger.info("Do Airplane findAll");
        return airplaneRepository.findAll();
    }

    public Optional<Airplane> findById(long id) {
        logger.info("Do Airplane findById " + id);
        return airplaneRepository.findById(id);
    }

    public List<Airplane> findAirplaneByAirlineId(long airlineId) throws AirlineNotFoundException {
        logger.info("Ini findAirplaneByAirlineId " + airlineId);
        Optional<Airline> airlineOptional = airlineService.findById(airlineId);
        if (airlineOptional.isPresent()) {
            logger.info("End findAirplaneByAirlineId " + airlineId);
            return airplaneRepository.findAirplaneByAirline(airlineOptional);
        } else {
            throw new AirlineNotFoundException();
        }
    }

    public AirplaneOutDto saveAirplane(Airplane airplane) {
        logger.info("Ini saveAirplane " + airplane);
        Airplane newAirplane = airplaneRepository.save(airplane);
        AirplaneOutDto airplaneOutDto = new AirplaneOutDto();
        modelMapper.map(newAirplane, airplaneOutDto);
        logger.info("End saveAirplane " + airplane);
        return airplaneOutDto;
    }

    public void removeAirplane(long airplaneId) throws AirplaneNotFoundException {
        logger.info("Ini removeAirplane ID: " + airplaneId);
        Airplane airplane = airplaneRepository.findById(airplaneId).orElseThrow(() -> new AirplaneNotFoundException(airplaneId));
        logger.info("End removeAirplane Airplane: " + airplane);
        airplaneRepository.delete(airplane);
    }

    public void modifyAirplane(Airplane newAirplane, long airplaneId) throws AirplaneNotFoundException {
        logger.info("Ini modifyAirplane ID: " + airplaneId);
        Optional<Airplane> airplane = airplaneRepository.findById(airplaneId);
        if (airplane.isPresent()) {
            Airplane existingAirplane = airplane.get();
            existingAirplane.setModel(newAirplane.getModel());
            existingAirplane.setManufacturingDate(newAirplane.getManufacturingDate());
            existingAirplane.setPassengerCapacity(newAirplane.getPassengerCapacity());
            existingAirplane.setMaxSpeed(newAirplane.getMaxSpeed());
            existingAirplane.setActive(newAirplane.isActive());
            existingAirplane.setAirline(newAirplane.getAirline());

            airplaneRepository.save(existingAirplane);
        } else {
            throw new AirplaneNotFoundException(airplaneId);
        }
        logger.info("End modifyAirplane Airplane: " + airplane);
    }

    public void patchAirplane(long airplaneId, AirplanePatchDto airplanePatchDto) throws AirplaneNotFoundException {
        logger.info("Ini patchAirplane ID: " + airplaneId);
        Airplane oldAirplane = airplaneRepository.findById(airplaneId).orElseThrow(AirplaneNotFoundException::new);
        if (airplanePatchDto.getField().equals("maxSpeed")) {
            oldAirplane.setMaxSpeed(airplanePatchDto.getMaxSpeed());
        }
        airplaneRepository.save((oldAirplane));
        logger.info("End patchAirplane");
    }
}