package com.programacion.distribuida.autores.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    private String nombre;

    @Column
    private Integer version;

}
