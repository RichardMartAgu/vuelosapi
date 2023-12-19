package com.svalero.vuelosapi.exceptions;

public class AirplaneNotFoundException extends Exception {

    public AirplaneNotFoundException() {
        super();
    }

    public AirplaneNotFoundException(String message) {
        super(message);
    }

    public AirplaneNotFoundException(long id) {
        super("El Avión con id" + id + " no existe");
    }
}

