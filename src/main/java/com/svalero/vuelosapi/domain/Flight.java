package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    @Min(value = 0, message = "La puerta de embarque debe ser mayor que cero")
    @NotNull(message = "La puerta de embarque es obligatoria")
    @Column
    private int gate;
    @Column
    @Positive
    private float duration;
    @Column
    @NotNull(message = "Si el vuelo es salido o llegada es obligatorio")
    private boolean departure;

    @ManyToOne
    @NotNull(message = "El Id del aeropuerto asociado es obligatorio")
    @JoinColumn(name = "airport_id")
    private Airport airport;
    @OneToMany(mappedBy = "flight")
    @JsonIgnore
    private List<Ticket> tickets;

}