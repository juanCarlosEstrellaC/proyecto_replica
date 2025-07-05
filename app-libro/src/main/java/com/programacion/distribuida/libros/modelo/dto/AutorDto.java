package com.programacion.distribuida.libros.modelo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AutorDto {

    private Integer id;
    private String nombre;

    public AutorDto() {
    }

    public AutorDto(Integer id, String nombre, Integer version) {
        this.id = id;
        this.nombre = nombre;
    }

}
