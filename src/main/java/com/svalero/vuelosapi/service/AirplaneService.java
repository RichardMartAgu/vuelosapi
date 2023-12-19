package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.Airplane;
import com.svalero.vuelosapi.exceptions.AirplaneNotFoundException;
import com.svalero.vuelosapi.repository.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;


    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    public Optional<Airplane> findById(long id) {
        return airplaneRepository.findById(id);
    }

    public void saveAirplane(Airplane airplane) {
        airplaneRepository.save(airplane);
    }

    public void removeAirplane(long airplaneId) throws AirplaneNotFoundException {
        Airplane airplane = airplaneRepository.findById(airplaneId).orElseThrow(()-> new AirplaneNotFoundException(airplaneId));
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

            airplaneRepository.save(existingAirplane);
        }
        else { throw new AirplaneNotFoundException (airplaneId);

        }

    }
}