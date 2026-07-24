plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.observability")
    id("cozyr.spring.autoconfigure")
    alias(libs.plugins.spring.boot)
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity-service:application"))
    implementation(project(":identity-service:domain"))
    implementation(project(":identity-service:jpa"))
    implementation(project(":identity-service:fileIO"))
    implementation(project(":identity-service:security"))
    implementation(project(":identity-service:web"))
    implementation(project(":identity-service:kafka"))
}