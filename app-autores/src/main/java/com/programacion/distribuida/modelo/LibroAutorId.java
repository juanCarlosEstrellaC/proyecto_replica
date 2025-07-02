package com.programacion.distribuida.modelo;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LibroAutorId {

    private String libroIsbn;
    private Integer autorId;

}
