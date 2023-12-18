package com.svalero.vuelosapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @Column
    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha tiene que ser mayor que el día de hoy")
    private LocalDate departureDate;
    @Min(value = 0, message = "La puerta de embarque ser mayor que cero")
    @Column
    private int gate;
    @Column
    private float duration;
    @Column
    private boolean international;

}