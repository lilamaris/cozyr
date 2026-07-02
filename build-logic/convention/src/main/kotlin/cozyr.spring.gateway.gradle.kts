import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(platform(libs.requireLibrary("spring-cloud-dependencies")))
    implementation(libs.requireLibrary("spring-cloud-starter-gateway-server-webflux"))
}