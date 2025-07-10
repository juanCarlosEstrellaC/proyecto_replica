############################ Dockerfile para la aplicacion de Autores ##############################
# Descargar una distribución de Linux ligera (Noble) que ya trae el JRE de OpenJDK (Eclipse Temurin),
FROM eclipse-temurin:21.0.7_6-jre-noble
ENV LANGUAGE='en_US:en'

# Crear un directorio para la aplicacion
RUN mkdir /app
WORKDIR /app

# Instalar utilidades necesarias, como ping, para verificar la conectividad de red.
RUN apt update
RUN apt install -y iputils-ping

# Copiar los archivos de la aplicacion al contenedor.
COPY build/quarkus-app/lib/ ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/app/ ./app
COPY build/quarkus-app/quarkus-run.jar app.jar

# Definir el comando para ejecutar la aplicación Java al iniciar el contenedor (java -jar app.jar).
CMD ["java", "-jar", "app.jar"]


############################ Comandos para construir la imagen Docker ##############################
#  1. Navegar al directorio donde se encuentra este Dockerfile.
#  2. Ejecutar el comando:
#       docker build -t jcestrella/app-libros:v1.0.0 .  (OJO, colocar el punto al final)
#
#  3. Para subir la imagen a Docker Hub, usar:
#       docker push jcestrella/app-libros:v1.0.0
