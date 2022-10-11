import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val restAssuredVersion = "4.4.0"
    val mockserverVersion = "5.13.2"
    val testcontainersVersion = "1.17.3"

    testImplementation(kotlin("test"))
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("org.testcontainers:mockserver:$testcontainersVersion")
    testImplementation("org.mock-server:mockserver-client-java:$mockserverVersion")
    testImplementation("javax.xml.bind:jaxb-api:2.3.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}