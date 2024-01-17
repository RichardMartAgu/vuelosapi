package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.Passenger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
        List<Passenger> findAll();
}
