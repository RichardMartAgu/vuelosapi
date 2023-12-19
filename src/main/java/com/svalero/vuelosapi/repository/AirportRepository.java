package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
    List<Airport> findAll();
}