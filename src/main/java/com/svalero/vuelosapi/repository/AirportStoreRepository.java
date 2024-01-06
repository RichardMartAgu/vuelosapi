package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.AirportStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportStoreRepository extends CrudRepository<AirportStore, Long> {
    List<AirportStore> findAll();

    List<AirportStore> findStoreByAirport(Optional<Airport> airport);
}