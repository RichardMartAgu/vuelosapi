package com.svalero.vuelosapi.exceptions;

public class PassengerNotFoundException extends Exception{
    public PassengerNotFoundException() {
        super();
    }

    public PassengerNotFoundException(String message) {
        super(message);
    }

    public PassengerNotFoundException(long id) {
        super("El Pasajero con id " + id + " no existe");
    }
}
