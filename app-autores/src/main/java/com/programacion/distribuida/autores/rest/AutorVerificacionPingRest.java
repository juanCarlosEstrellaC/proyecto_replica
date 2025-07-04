package com.programacion.distribuida.autores.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ping")
public class AutorVerificacionPingRest {

    @GET
    public String ping() {
        return "pong";
    }
}
