package com.programacion.distribuida;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AutorCicloDeVida {

    // Inyección de propiedades de configuración
    @Inject
    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8080")
    Integer httpPort;

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "localhost")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    Integer consulPort;

    // ID del servicio (una instancia de app-autores). Generará un id único para identificar el servicio en Consul.
    String idServicio;

    // Metodo para registrar el servicio en Consul al iniciar la aplicación.
    void registrarServicioEnConsul(@Observes StartupEvent evento, Vertx vertx){ // TODO quité el throws Exception, revisar si es necesario
        try {
            System.out.println("Iniciando servicio de Autores...");
            ConsulClientOptions opciones = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient clienteConsul = ConsulClient.create(vertx, opciones);

            // Generar un ID único para el servicio
            idServicio = UUID.randomUUID().toString();

            // Definir las etiquetas del servicio para que Traefik pueda enrutar las peticiones.
            var etiquetas = List.of(
                    "traefik.enable=true",
                    "traefik.http.routers.app-autores.rule=PathPrefix(`/app-autores`)",
                    "traefik.http.routers.app-autores.middlewares=strip-prefix-autores",
                    "traefik.http.middlewares.strip-prefix-autores.stripPrefix.prefixes=/app-autores"
            );

            // Configurar las opciones de verificación del servicio. Verifica que el servicio esté activo y respondiendo.
            var opcionesVerificacion = new CheckOptions()
                    .setHttp(String.format("http://%s:%d/ping", InetAddress.getLocalHost().getHostName(), httpPort))
                    .setInterval("10s")
                    .setDeregisterAfter("20s");

            // Registrar el servicio en Consul
            ServiceOptions opcionesServicio = new ServiceOptions()
                    .setId(idServicio)
                    .setName("app-autores")
                    .setAddress(InetAddress.getLocalHost().getHostAddress())
                    .setPort(httpPort)
                    .setTags(etiquetas)
                    .setCheckOptions(opcionesVerificacion);
            clienteConsul.registerServiceAndAwait(opcionesServicio);

        } catch (Exception ex) {
            System.err.println("Error al iniciar el servicio de autores: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Metodo para eliminar el servicio de Consul al detener la aplicación.
    void eliminarServicioDeConsul(@Observes ShutdownEvent evento, Vertx vertx) {
        try {
            System.out.println("Deteniendo servicio de Autores...");
            ConsulClientOptions opciones = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient clienteConsul = ConsulClient.create(vertx, opciones);

            // Desregistrar el servicio de Consul
            clienteConsul.deregisterServiceAndAwait(idServicio);
        } catch (Exception ex) {
            System.err.println("Error al detener el servicio de autores: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
