import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    runtimeOnly(libs.requireLibrary("postgresql"))

    testImplementation(libs.requireLibrary("spring-boot-testcontainers"))
    testImplementation(libs.requireLibrary("testcontainers-postgresql"))
}