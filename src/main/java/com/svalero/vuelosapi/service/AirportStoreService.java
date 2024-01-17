package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.AirlineController;
import com.svalero.vuelosapi.domain.*;
import com.svalero.vuelosapi.dto.AirportStoreOutDto;
import com.svalero.vuelosapi.dto.AirportStorePatchDto;
import com.svalero.vuelosapi.exceptions.AirportNotFoundException;
import com.svalero.vuelosapi.exceptions.AirportStoreNotFoundException;
import com.svalero.vuelosapi.repository.AirportStoreRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportStoreService {

    @Autowired
    private AirportStoreRepository airportStoreRepository;
    @Autowired
    private AirportService airportService;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(AirlineController.class);


    public List<AirportStore> findAll() {
        logger.info("Do AirportStore findAll");
        return airportStoreRepository.findAll();
    }

    public Optional<AirportStore> findById(long id) {
        logger.info("Do AirportStore findById " + id);
        return airportStoreRepository.findById(id);
    }

    public List<AirportStore> findStoreByAirportId(long airportId) throws AirportNotFoundException {
        logger.info("Ini findStoreByAirportId " + airportId);
        Optional<Airport> airportOptional = airportService.findById(airportId);
        if (airportOptional.isPresent()) {
            logger.info("End findStoreByAirportId " + airportId);
            return airportStoreRepository.findStoreByAirport(airportOptional);
        } else {
            throw new AirportNotFoundException();
        }
    }

    public AirportStoreOutDto saveAirportStore(AirportStore airportStore) {
        logger.info("Ini saveAirportStore " + airportStore);
        AirportStore newAirportStore = airportStoreRepository.save(airportStore);
        AirportStoreOutDto airportStoreOutDto = new AirportStoreOutDto();
        modelMapper.map(newAirportStore, airportStoreOutDto);
        logger.info("End saveAirportStore " + airportStore);
        return airportStoreOutDto;
    }

    public void removeAirportStore(long airportStoreId) throws AirportStoreNotFoundException {
        logger.info("Ini removeAirportStore ID: " + airportStoreId);
        AirportStore airportStore = airportStoreRepository.findById(airportStoreId).orElseThrow(() -> new AirportStoreNotFoundException(airportStoreId));
        logger.info("End removeAirportStore AirportStore: " + airportStore);
        airportStoreRepository.delete(airportStore);
    }

    public void modifyAirportStore(AirportStore newAirportStore, long airportStoreId) throws AirportStoreNotFoundException {
        logger.info("Ini modifyAirportStore ID: " + airportStoreId);
        Optional<AirportStore> airportStore = airportStoreRepository.findById(airportStoreId);
        if (airportStore.isPresent()) {
            AirportStore existingAirportStore = airportStore.get();
            existingAirportStore.setName(newAirportStore.getName());
            existingAirportStore.setType(newAirportStore.getType());
            existingAirportStore.setTelephone(newAirportStore.getTelephone());
            existingAirportStore.setAverageProfit(newAirportStore.getAverageProfit());
            existingAirportStore.setOpeningDay(newAirportStore.getOpeningDay());
            existingAirportStore.setOpen(newAirportStore.isOpen());
            existingAirportStore.setAirport(newAirportStore.getAirport());

            airportStoreRepository.save(existingAirportStore);
        } else {
            throw new AirportStoreNotFoundException(airportStoreId);
        }
        logger.info("End modifyAirportStore AirportStore: " + airportStore);
    }

    public void patchAirportStore(long airportStoreId, AirportStorePatchDto airportStorePatchDto) throws AirportStoreNotFoundException {
        logger.info("Ini patchAirportStore ID: " + airportStoreId);
        AirportStore oldAirportStore = airportStoreRepository.findById(airportStoreId).orElseThrow(AirportStoreNotFoundException::new);
        if (airportStorePatchDto.getField().equals("type")) {
            oldAirportStore.setType(airportStorePatchDto.getType());
        }
        airportStoreRepository.save((oldAirportStore));
        logger.info("End patchAirportStore");
    }
}