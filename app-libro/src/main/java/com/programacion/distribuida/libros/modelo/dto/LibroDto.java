package com.programacion.distribuida.libros.modelo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class LibroDto {

    private String isbn;
    private String titulo;
    private BigDecimal precio;

    // Estos campos representan el inventario del libro
    private Integer inventarioVendido;
    private Integer inventarioSuministrado;

    // Lista de autores del libro (solo nombres, no objetos completos).
    private List<String> autores;
}
