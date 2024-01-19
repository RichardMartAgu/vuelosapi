package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airport")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @NotNull(message = "La ciudad es obligatoria")
    @Column
    private String city;
    @NotNull(message = "El año de fundación es obligatoria")
    @Column
    private LocalDate foundationYear;
    @Column
    @NotNull
    private double latitude;
    @Column
    @NotNull
    private double longitude;
    @Column
    private boolean active;

    @ToString.Exclude
    @OneToMany(mappedBy = "airport")
    @JsonIgnore
    private List<AirportStore> airportStores;
    @ToString.Exclude
    @OneToMany(mappedBy = "airport")
    @JsonIgnore
    private List<Flight> flights;

}