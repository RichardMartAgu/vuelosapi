package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRepository extends CrudRepository<Airline, Long> {
    List<Airline> findAll();
}