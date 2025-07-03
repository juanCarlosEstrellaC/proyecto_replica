package com.programacion.distribuida.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LibroAutorId {

    @Column(name = "libro_isbn")
    private String libroIsbn;

    @Column(name = "autor_id")
    private Integer autorId;

}
