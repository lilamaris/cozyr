import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-actuator"))
    implementation(libs.requireLibrary("micrometer-registry-prometheus"))
    implementation(libs.requireLibrary("spring-boot-starter-opentelemetry"))
    implementation(libs.requireLibrary("datasource-micrometer-spring-boot"))
    implementation(libs.requireLibrary("datasource-micrometer-opentelemetry"))
}