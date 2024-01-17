package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column
    private String name;
    @NotNull(message = "El apellido es obligatorio")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Column
    private String surname;
    @NotNull(message = "El dni es obligatorio")
    @NotBlank(message = "El dni no puede estar vacío")
    @Size(max = 10, message = "El dni no puede tener más de 10 caracteres")
    @Column
    private String dni;
    @Column
    private LocalDate birthday;
    @Column
    private int age;


    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    private List<Ticket> tickets;
}
