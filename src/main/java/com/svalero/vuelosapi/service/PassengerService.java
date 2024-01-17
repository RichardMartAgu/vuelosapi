package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.PassengerController;
import com.svalero.vuelosapi.domain.Passenger;
import com.svalero.vuelosapi.dto.PassengerPatchDto;
import com.svalero.vuelosapi.exceptions.PassengerNotFoundException;
import com.svalero.vuelosapi.repository.PassengerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {


    @Autowired
    private PassengerRepository passengerRepository;
    private Logger logger = LoggerFactory.getLogger(PassengerController.class);


    public List<Passenger> findAll() {
        logger.info("Do Passenger findAll");
        return passengerRepository.findAll();
    }

    public Optional<Passenger> findById(long id) {
        logger.info("Do Passenger findById " + id);
        return passengerRepository.findById(id);
    }

    public Passenger savePassenger(Passenger passenger) {
        logger.info("Ini savePassenger " + passenger);
        passengerRepository.save(passenger);
        logger.info("End savePassenger " + passenger);
        return passenger;
    }

    public void removePassenger(long passengerId) throws PassengerNotFoundException {
        logger.info("Ini removePassenger ID: " + passengerId);
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> new PassengerNotFoundException(passengerId));
        logger.info("End removePassenger Passenger: " + passenger);
        passengerRepository.delete(passenger);
    }

    public void modifyPassenger(Passenger newPassenger, long passengerId) throws PassengerNotFoundException {
        logger.info("Ini modifyPassenger ID: " + passengerId);
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (passenger.isPresent()) {
            Passenger existingPassenger = passenger.get();
            existingPassenger.setName(newPassenger.getName());
            existingPassenger.setSurname(newPassenger.getSurname());
            existingPassenger.setDni(newPassenger.getDni());
            existingPassenger.setBirthday(newPassenger.getBirthday());
            existingPassenger.setAge(newPassenger.getAge());
            passengerRepository.save(existingPassenger);
        } else {
            throw new PassengerNotFoundException(passengerId);
        }
        logger.info("End modifyPassenger Passenger: " + passenger);
    }

    public void patchPassenger(long passengerId, PassengerPatchDto passengerPatchDto) throws PassengerNotFoundException {
        logger.info("Ini patchPassenger ID: " + passengerId);
        Passenger oldPassenger = passengerRepository.findById(passengerId).orElseThrow(PassengerNotFoundException::new);
        if (passengerPatchDto.getField().equals("name")) {
            oldPassenger.setName(passengerPatchDto.getName());
        }
        passengerRepository.save((oldPassenger));
        logger.info("End patchPassenger");
    }

}

