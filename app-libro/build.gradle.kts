plugins {
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("io.quarkus") version "3.22.2"
}

group = "com.programacion.distribuida"
version = "1.0-SNAPSHOT"
val versionQuarkus = "3.22.2"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Quarkus
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:$versionQuarkus"))

    // CDI
    implementation("io.quarkus:quarkus-arc")

    // REST
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jsonb")
    // Rest Client: para consumir servicios REST
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-rest-client-jsonb")

    // JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")

    // Model Mapper
    implementation("org.modelmapper:modelmapper:3.2.3")

    // Service Discovery dinámico con Consul
    implementation("io.quarkus:quarkus-smallrye-stork")
    implementation("io.smallrye.stork:stork-service-discovery-consul")
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client") // Mutiny para programación reactiva

    // Quarkus Fault Tolerance
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")

    // Telemetria: metricas
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-jackson")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}