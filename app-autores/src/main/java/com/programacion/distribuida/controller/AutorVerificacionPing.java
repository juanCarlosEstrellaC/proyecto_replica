package com.programacion.distribuida.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ping")
public class AutorVerificacionPing {

    @GET
    public String ping() {
        return "pong";
    }
}
