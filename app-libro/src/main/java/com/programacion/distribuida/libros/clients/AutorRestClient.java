package com.programacion.distribuida.libros.clients;

import com.programacion.distribuida.libros.modelo.dto.AutorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// Este cliente REST se registra con Stork para ser utilizado en la comunicación con el servicio de autores
// El URI base es una referencia a un servicio registrado en Stork en el application.properties.
// Esta es una de tantas configuraciones de stork: quarkus.stork.servicio-autores.service-discovery.type= consul
@RegisterRestClient(baseUri = "stork://servicio-autores")
public interface AutorRestClient {

    @GET
    @Path("/libro/{isbn}")
    List<AutorDto> buscarPorLibro(@PathParam("isbn") String isbn);
}
