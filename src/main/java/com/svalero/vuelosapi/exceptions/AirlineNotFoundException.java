package com.svalero.vuelosapi.exceptions;

public class AirlineNotFoundException extends Exception {

    public AirlineNotFoundException() {
        super();
    }

    public AirlineNotFoundException(String message) {
        super(message);
    }

    public AirlineNotFoundException(long id) {
        super("La Aerolínea con id " + id + " no existe");
    }
}

