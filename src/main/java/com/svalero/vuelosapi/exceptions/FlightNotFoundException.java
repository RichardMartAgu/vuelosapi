package com.svalero.vuelosapi.exceptions;

public class FlightNotFoundException extends Exception {

    public FlightNotFoundException() {
        super();
    }

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(long id) {
        super("El producto " + id + " no existe");
    }
}

