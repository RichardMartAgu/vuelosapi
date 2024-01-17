package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.Airplane;
import com.svalero.vuelosapi.dto.AirlinePatchDto;
import com.svalero.vuelosapi.dto.AirplaneOutDto;
import com.svalero.vuelosapi.dto.AirplanePatchDto;
import com.svalero.vuelosapi.exceptions.AirlineNotFoundException;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.repository.AirplaneRepository;
import org.modelmapper.ModelMapper;
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

    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    public Optional<Airplane> findById(long id) {
        return airplaneRepository.findById(id);
    }

    public List<Airplane> findAirplaneByAirlineId(long airlineId) throws AirlineNotFoundException {
        Optional<Airline> airlineOptional = airlineService.findById(airlineId);
        if (airlineOptional.isPresent()) {
            return airplaneRepository.findAirplaneByAirline(airlineOptional);
        } else {
            throw new AirlineNotFoundException();
        }
    }

    public AirplaneOutDto saveAirplane(Airplane airplane) {
        Airplane newAirplane = airplaneRepository.save(airplane);
        AirplaneOutDto airplaneOutDto = new AirplaneOutDto();
        modelMapper.map(newAirplane, airplaneOutDto);
        return airplaneOutDto;
    }

    public void removeAirplane(long airplaneId) throws AirplaneNotFoundException {
        Airplane airplane = airplaneRepository.findById(airplaneId).orElseThrow(() -> new AirplaneNotFoundException(airplaneId));
        airplaneRepository.delete(airplane);
    }

    public void modifyAirplane(Airplane newAirplane, long airplaneId) throws AirplaneNotFoundException {
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

    }
    public void patchAirplane (long airplaneId, AirplanePatchDto airplanePatchDto) throws AirplaneNotFoundException {
        Airplane oldAirplane = airplaneRepository.findById(airplaneId).orElseThrow(AirplaneNotFoundException::new);
        if(airplanePatchDto.getField().equals("maxSpeed")){
            oldAirplane.setMaxSpeed(airplanePatchDto.getMaxSpeed());
        }
        airplaneRepository.save((oldAirplane));
    }
}