package com.svalero.vuelosapi.repository;

import com.svalero.vuelosapi.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findAll();
    List<Ticket> findTicketByPassenger(Optional<Passenger> passenger);
    List<Ticket> findTicketByFlight(Optional<Flight> flight);
}
