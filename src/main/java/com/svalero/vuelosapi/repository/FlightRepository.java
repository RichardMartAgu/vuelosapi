package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Airport;
import com.svalero.vuelosapi.domain.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findAll();

    List<Flight> findFlightByAirport(Optional<Airport> airport);
}