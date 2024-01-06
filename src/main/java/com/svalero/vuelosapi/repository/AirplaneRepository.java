package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Airline;
import com.svalero.vuelosapi.domain.Airplane;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirplaneRepository extends CrudRepository<Airplane, Long> {
    List<Airplane> findAll();

    List<Airplane> findAirplaneByAirline(Optional<Airline> airline);
}