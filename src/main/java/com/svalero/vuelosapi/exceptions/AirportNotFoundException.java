package com.svalero.vuelosapi.exceptions;

public class AirportNotFoundException extends Exception {

    public AirportNotFoundException() {
        super();
    }

    public AirportNotFoundException(String message) {
        super(message);
    }

    public AirportNotFoundException(long id) {
        super("El Aeropuerto con" + id + " no existe");
    }
}

