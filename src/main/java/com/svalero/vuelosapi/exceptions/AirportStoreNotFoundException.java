package com.svalero.vuelosapi.exceptions;

public class AirportStoreNotFoundException extends Exception {

    public AirportStoreNotFoundException() {
        super();
    }

    public AirportStoreNotFoundException(String message) {
        super(message);
    }

    public AirportStoreNotFoundException(long id) {
        super("La tienda con id " + id + " no existe");
    }
}

