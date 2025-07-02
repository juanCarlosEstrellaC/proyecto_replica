plugins {
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("io.quarkus") version "3.22.2"
}

group = "com.programacion.distribuida"
version = "1.0-SNAPSHOT"
val versionQuarkus = "3.22.2"

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

    // JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")

    // Control de versiones Flyway
    implementation("io.quarkus:quarkus-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Service Discovery dinámico con Consul
    implementation("io.quarkus:quarkus-smallrye-stork")
    implementation("io.smallrye.stork:stork-service-discovery-consul")
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client") // Mutiny para programación reactiva

    // Telemetria: metricas
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-jackson")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")

}