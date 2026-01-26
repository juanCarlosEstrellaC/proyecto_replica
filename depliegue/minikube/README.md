# Guía de Minikube y Acceso a Servicios

## 🚀 Comandos Esenciales de Minikube

Aquí tienes un resumen rápido para gestionar tu clúster local.

### 📍 ¿Desde dónde ejecutar estos comandos?
- **Comandos de Gestión/Consulta** (`minikube start`, `get pods`): Puedes ejecutarlos desde **cualquier carpeta** en tu terminal.
- **Comandos de Despliegue** (`kubectl apply -f .`): Debes ejecutarlos **dentro de la carpeta** donde están tus archivos `.yml` (en este caso `/depliegue/minikube`).

### Gestión del Clúster
```bash
# Iniciar el clúster
minikube start

# Detener el clúster
minikube stop

# Ver estado general
minikube status

# Abrir el dashboard visual en el navegador
minikube dashboard
```

### Inspección de Recursos (Kubectl)
Nota: Si tienes `kubectl` instalado independientemente, puedes omitir `minikube` y el guion `--`.

```bash
# Ver todos los Pods en ejecución
minikube kubectl -- get pods

# Ver los servicios y sus puertos
minikube kubectl -- get svc

# Ver logs de un pod específico (útil para errores)
minikube kubectl -- logs <nombre_del_pod>

# Ver descripción detallada de un pod (para debug avanzado)
minikube kubectl -- describe pod <nombre_del_pod>
```

---

## 🔌 Establecer Túneles (Port-Forwarding)

Para acceder a los servicios que no están expuestos externamente o para desarrollo local, utilizamos `kubectl port-forward`.

**Sintaxis General:**
```bash
minikube kubectl -- port-forward service/<nombre_del_servicio> <puerto_mi_pc>:<puerto_traefik>
```

### Comandos Útiles

#### 1. Traefik Proxy (Dashboard y Tráfico HTTP)
Mapea el tráfico HTTP general y el dashboard de administración de Traefik.

```bash
minikube kubectl -- port-forward service/traefik-proxy 8080:8080 9000:8888
```

- **Dashboard de Traefik:** [http://localhost:9000/dashboard/](http://localhost:9000/dashboard/)
- **App Autores (vía Traefik):** [http://localhost:8080/mi-app-autores/autores](http://localhost:8080/mi-app-autores/autores)
- **App Libros (vía Traefik):** [http://localhost:8080/mi-app-libros/libros](http://localhost:8080/mi-app-libros/libros)

#### 2. Servicio de Autores (Directo)
Acceso directo al microservicio de autores, saltándose el proxy.

```bash
minikube kubectl -- port-forward service/autores 8081:8080
```
- **URL:** [http://localhost:8081/autores](http://localhost:8081/autores)

#### 3. Servicio de Libros (Directo)
Acceso directo al microservicio de libros, saltándose el proxy.

```bash
minikube kubectl -- port-forward service/libros 8082:8080
```
- **URL:** [http://localhost:8082/libros](http://localhost:8082/libros)

#### 4. Consul UI
Acceso a la interfaz gráfica de Consul para ver el estado de los servicios.

```bash
minikube kubectl -- port-forward service/consul 8500:8500
```
- **URL:** [http://localhost:8500/ui](http://localhost:8500/ui)

#### 5. Prometheus
Acceso a la interfaz de métricas de Prometheus.

```bash
minikube kubectl -- port-forward service/prometheus-metricas 9090:9090
```
- **URL:** [http://localhost:9090](http://localhost:9090)

#### 6. Grafana
Acceso a los dashboards de visualización en Grafana.

```bash
minikube kubectl -- port-forward service/grafana-dashboard 3000:3000
```
- **URL:** [http://localhost:3000](http://localhost:3000)

> **Nota:** Para abrir túneles simultáneos, asegúrate de usar puertos diferentes en tu PC para evitar colisiones.
