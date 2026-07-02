import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-webmvc"))
    implementation(libs.requireLibrary("spring-boot-starter-validation"))
    implementation(libs.requireLibrary("springdoc-openapi-starter-webmvc-ui"))

    testImplementation(libs.requireLibrary("spring-boot-starter-webmvc-test"))
}