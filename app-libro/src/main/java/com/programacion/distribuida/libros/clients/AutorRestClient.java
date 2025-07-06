package com.programacion.distribuida.libros.clients;

import com.programacion.distribuida.libros.modelo.dto.AutorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * Este cliente REST se registra con Stork para ser utilizado en la comunicación con el servicio de autores
 * El URI base es una referencia a un servicio registrado en Stork en el application.properties.
 * Esta es una de tantas configuraciones de stork: quarkus.stork.servicio-autores.service-discovery.type= consul
 */
@Path("/autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(baseUri = "stork://servicio-autores")
public interface AutorRestClient {

    /**
     * Discrepancia entre Autor y AutorDto
     * Jackson mapea automáticamente los campos coincidentes del JSON de Autor a AutorDto, ignorando los que no existen
     * en el DTO.
     */
    @GET
    @Path("/libro/{isbn}")
    @Retry(maxRetries = 2, delay = 0)
    @Fallback(fallbackMethod = "buscarPorLibroFallback")
    List<AutorDto> buscarPorLibro(@PathParam("isbn") String isbn);

    /**
     * Metodo de fallback que se invoca si falla el metodo original.
     * Retorna un autor ficticio para evitar que la aplicación falle.
     */
    default List<AutorDto> buscarPorLibroFallback(String isbn) {
        var autorDto = new AutorDto();
        autorDto.setId(0);
        autorDto.setNombre("Nombre NO disponible. Esta es respuesta del Fallback");
        return List.of(autorDto);
    }

}
