package com.svalero.vuelosapi.exceptions;

public class TicketNotFoundException extends Exception{
    public TicketNotFoundException() {
        super();
    }

    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(long id) {
        super("El Ticket con id " + id + " no existe");
    }
}
