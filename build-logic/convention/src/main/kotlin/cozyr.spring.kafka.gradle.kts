import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-kafka"))

    testImplementation(libs.requireLibrary("spring-boot-starter-kafka"))
}