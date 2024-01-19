package com.svalero.vuelosapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre del asiento es obligatorio")
    @NotBlank(message = "El nombre del asiento no puede estar vacío")
    @Column
    private String seatNumber;
    @NotNull(message = "La fecha de expedición es obligatorio")
    @Column
    private LocalDate issuing;
    @NotNull(message = "Numero de equipaje total obligatorio")
    @Min(value = 0, message = "El número de equipaje no puede ser menor que 0")
    @Column
    private int baggage;

    @Column
    private boolean premium;
    @ManyToOne
    @NotNull(message = "El Id del pasajero asociado es obligatorio")
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @NotNull(message = "El Id del vuelo asociado es obligatorio")
    @JoinColumn(name = "flight_id")
    private Flight flight;


}
