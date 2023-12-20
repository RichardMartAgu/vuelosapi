package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.AirportStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportStoreRepository extends CrudRepository<AirportStore, Long> {
    List<AirportStore> findAll();
}