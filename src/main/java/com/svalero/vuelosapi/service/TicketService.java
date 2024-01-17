package com.svalero.vuelosapi.service;

import com.svalero.vuelosapi.controller.TicketController;
import com.svalero.vuelosapi.domain.Flight;
import com.svalero.vuelosapi.domain.Passenger;
import com.svalero.vuelosapi.domain.Ticket;
import com.svalero.vuelosapi.dto.TicketOutDto;
import com.svalero.vuelosapi.dto.TicketPatchDto;
import com.svalero.vuelosapi.exceptions.TicketNotFoundException;
import com.svalero.vuelosapi.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(TicketController.class);

    public List<Ticket> findAll() {
        logger.info("Do Ticket findAll");
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(long id) {
        logger.info("Do Ticket findById " + id);
        return ticketRepository.findById(id);
    }

    public List<Ticket> findTicketByPassengerId(long pasangerId) throws TicketNotFoundException {
        logger.info("Ini findTicketByPassengerId " + pasangerId);
        Optional<Passenger> passengerOptional = passengerService.findById(pasangerId);
        if (passengerOptional.isPresent()) {
            logger.info("End findTicketByPassengerId " + pasangerId);
            return ticketRepository.findTicketByPassengerId(passengerOptional);
        } else {
            throw new TicketNotFoundException();
        }
    }
    public List<Ticket> findTicketByFlightId(long flightId) throws TicketNotFoundException {
        logger.info("Ini findTicketByFlightId " + flightId);
        Optional<Flight> flightOptional = flightService.findById(flightId);
        if (flightOptional.isPresent()) {
            logger.info("End findTicketByFlightId " + flightId);
            return ticketRepository.findTicketByFlightId(flightOptional);
        } else {
            throw new TicketNotFoundException();
        }
    }

    public TicketOutDto saveTicket(Ticket ticket) {
        logger.info("Ini saveTicket " + ticket);
        Ticket newTicket = ticketRepository.save(ticket);
        TicketOutDto ticketOutDto = new TicketOutDto();
        modelMapper.map(newTicket, ticketOutDto);
        logger.info("End saveTicket " + ticket);
        return ticketOutDto;
    }

    public void removeTicket(long ticketId) throws TicketNotFoundException {
        logger.info("Ini removeTicket ID: " + ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
        logger.info("End removeTicket Ticket: " + ticket);
        ticketRepository.delete(ticket);
    }

    public void modifyTicket(Ticket newTicket, long ticketId) throws TicketNotFoundException {
        logger.info("Ini modifyTicket ID: " + ticketId);
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            Ticket existingTicket = ticket.get();
            existingTicket.setSeatNumber(newTicket.getSeatNumber());
            existingTicket.setIssuing(newTicket.getIssuing());
            existingTicket.setPremium(newTicket.isPremium());
            existingTicket.setPassenger(newTicket.getPassenger());
            existingTicket.setFlight(newTicket.getFlight());


            ticketRepository.save(existingTicket);
        } else {
            throw new TicketNotFoundException(ticketId);
        }
        logger.info("End modifyTicket Ticket: " + ticket);
    }

    public void patchTicket(long ticketId, TicketPatchDto ticketPatchDto) throws TicketNotFoundException {
        logger.info("Ini patchTicket ID: " + ticketId);
        Ticket oldTicket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
        if (ticketPatchDto.getField().equals("seatNumber")) {
            oldTicket.setSeatNumber(ticketPatchDto.getSeatNumber());
        }
        ticketRepository.save((oldTicket));
        logger.info("End patchTicket");
    }
}

