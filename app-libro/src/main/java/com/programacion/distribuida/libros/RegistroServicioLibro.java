package com.programacion.distribuida.libros;

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

/**
 * Ciclo de vida del servicio de Libros
 * Registra el servicio en Consul al iniciar la aplicación y lo elimina al detenerla.
 * Este ciclo de vida es necesario para que Traefik pueda enrutar las peticiones al servicio de Libros.
 *
 * Consul es un servicio que registra, descubre y monitorea la salud de los servicios en tu red.
 * Traefik es un proxy inverso y balanceador de carga (a nivel de red) que dirige automáticamente el tráfico al servicio
 * correcto según reglas y descubrimiento.
 */

@ApplicationScoped
public class RegistroServicioLibro {

    // Inyección de propiedades de configuración
    @Inject
    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8080") // TODO: cambie del 9090. Ese solo para modo dev.
    Integer httpPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.host", defaultValue = "localhost")
    String httpHost;

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "localhost")
    String consulHost;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    Integer consulPort;

    // ID del servicio (una instancia de app-autores). Generará un id único para identificar el servicio en Consul.
    String idServicio;

    // Metodo para registrar el servicio en Consul al iniciar la aplicación. Se ejecuta al inicio de la aplicación
    // automáticamente gracias a la anotación @Observes en el evento StartupEvent.
    void registrarServicioEnConsul(@Observes StartupEvent eventoInicioApp, Vertx vertx) { // TODO quité el throws Exception, revisar si es necesario
        try {
            System.out.println("Iniciando servicio de Libros...");
            ConsulClientOptions opciones = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient clienteConsul = ConsulClient.create(vertx, opciones);

            // Generar un ID único para el servicio
            idServicio = UUID.randomUUID().toString();

            // Definir las etiquetas del servicio para que Traefik pueda enrutar las peticiones.
            var etiquetas = List.of(
                    "traefik.enable=true",
                    "traefik.http.routers.mi-enrutador-app-libros.rule=PathPrefix(`/mi-app-libros`)",
                    "traefik.http.routers.mi-enrutador-app-libros.middlewares=strip-prefix-libros",
                    "traefik.http.middlewares.strip-prefix-libros.stripPrefix.prefixes=/mi-app-libros"
            );

            /* Configurar las opciones de verificación del servicio (healthcheck de Consul). Verifica que el servicio
               esté activo y respondiendo. La verificación se realiza enviando una petición HTTP al endpoint /ping del
               servicio. Si el servicio no responde, Consul lo considerará inactivo y lo eliminará de su registro
               después de 20 segundos. */
            var direccionIp = InetAddress.getLocalHost();
            System.out.println("Direccion ip:" + direccionIp.getHostAddress());
            System.out.println("Puerto HTTP del servicio: " + httpPort);
            System.out.println("Host HTTP del servicio: " + httpHost);
            var opcionesVerificacion = new CheckOptions()
                    // TODO: Para contenedores se usa la IP del contenedor. Usar localhost sin contenedores.
                    //.setHttp("http://127.0.0.1:8080/ping")
                    .setHttp(String.format("http://%s:%s/ping", httpHost, httpPort)) // TODO: Sin contenedores, usar localhost.
                    //.setHttp(String.format("http://%s:%s/ping", direccionIp.getHostAddress(), httpPort))
                    .setInterval("10s")
                    .setDeregisterAfter("20s");

            // Registrar el servicio en Consul
            ServiceOptions opcionesServicio = new ServiceOptions()
                    .setId(idServicio)
                    .setName("app-libros")
                    //.setAddress(InetAddress.getLocalHost().getHostAddress())
                    .setAddress(httpHost) // TODO: Sin contenedores, usar localhost.
                    .setPort(httpPort)
                    .setTags(etiquetas)
                    .setCheckOptions(opcionesVerificacion);
            clienteConsul.registerServiceAndAwait(opcionesServicio);

        } catch (Exception ex) {
            System.err.println("Error al iniciar el servicio de libros: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Metodo para eliminar el servicio de Consul al detener la aplicación. Se ejecuta al finalizar la aplicación
    // automáticamente gracias a la anotación @Observes en el evento ShutdownEvent.
    void eliminarServicioDeConsul(@Observes ShutdownEvent eventoFinApp, Vertx vertx) {
        try {
            System.out.println("Deteniendo servicio de Libros...");
            ConsulClientOptions opciones = new ConsulClientOptions()
                    .setHost(consulHost)
                    .setPort(consulPort);
            ConsulClient clienteConsul = ConsulClient.create(vertx, opciones);

            // Desregistrar el servicio de Consul
            clienteConsul.deregisterServiceAndAwait(idServicio);
        } catch (Exception ex) {
            System.err.println("Error al detener el servicio de libros: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
