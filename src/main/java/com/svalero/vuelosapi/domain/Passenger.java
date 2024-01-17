package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

public class Passenger  {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private int age;
    private String email;

    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    private List<Ticket> tickets;
}
