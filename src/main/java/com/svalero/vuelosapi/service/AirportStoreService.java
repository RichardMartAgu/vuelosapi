package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.domain.AirportStore;
import com.svalero.vuelosapi.exceptions.AirportStoreNotFoundException;
import com.svalero.vuelosapi.repository.AirportStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportStoreService {

    @Autowired
    private AirportStoreRepository airportStoreRepository;


    public List<AirportStore> findAll() {
        return airportStoreRepository.findAll();
    }

    public Optional<AirportStore> findById(long id) {
        return airportStoreRepository.findById(id);
    }

    public void saveAirportStore(AirportStore airportStore) {
        airportStoreRepository.save(airportStore);
    }

    public void removeAirportStore(long airportStoreId) throws AirportStoreNotFoundException {
        AirportStore airportStore = airportStoreRepository.findById(airportStoreId).orElseThrow(()-> new AirportStoreNotFoundException(airportStoreId));
        airportStoreRepository.delete(airportStore);
    }

    public void modifyAirportStore(AirportStore newAirportStore, long airportStoreId) throws AirportStoreNotFoundException {
        Optional<AirportStore> airportStore = airportStoreRepository.findById(airportStoreId);
        if (airportStore.isPresent()) {
            AirportStore existingAirportStore = airportStore.get();
            existingAirportStore.setName(newAirportStore.getName());
            existingAirportStore.setType(newAirportStore.getType());
            existingAirportStore.setTelephone(newAirportStore.getTelephone());
            existingAirportStore.setAverageProfit(newAirportStore.getAverageProfit());
            existingAirportStore.setOpeningDay(newAirportStore.getOpeningDay());
            existingAirportStore.setOpen(newAirportStore.isOpen());

            airportStoreRepository.save(existingAirportStore);
        }
        else { throw new AirportStoreNotFoundException (airportStoreId);

        }

    }
}