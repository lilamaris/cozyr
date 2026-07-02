import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-data-redis"))
    implementation(libs.requireLibrary("tools-jackson-databind"))

    testImplementation(libs.requireLibrary("spring-boot-testcontainers"))
    testImplementation(libs.requireLibrary("spring-boot-starter-data-redis-test"))
}