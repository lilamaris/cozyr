import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-boot-starter-security"))
    implementation(libs.requireLibrary("spring-boot-starter-oauth2-resource-server"))

    testImplementation(libs.requireLibrary("spring-security-test"))
}