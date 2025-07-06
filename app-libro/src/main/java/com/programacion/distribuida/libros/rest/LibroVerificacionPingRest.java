package com.programacion.distribuida.libros.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ping")
public class LibroVerificacionPingRest {

    @GET
    public String ping() {
        return "pong";
    }
}
