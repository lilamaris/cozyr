import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-validation"))
    implementation(libs.requireLibrary("spring-boot-autoconfigure"))
    annotationProcessor(libs.requireLibrary("spring-boot-configuration-processor"))
}