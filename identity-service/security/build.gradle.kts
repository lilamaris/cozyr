plugins {
    id("cozyr.java.module")
    id("cozyr.spring.platform")
    id("cozyr.spring.security")
    id("cozyr.spring.autoconfigure")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity-service:domain"))
    implementation(project(":identity-service:application"))
    implementation(project(":identity-service:identity-resource-server"))

    implementation(libs.spring.boot.starter.oauth2.client)
    implementation(libs.tools.jackson.databind)
    implementation(libs.jakarta.servlet.api)
}