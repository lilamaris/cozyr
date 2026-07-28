plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-application"))
    api(project(":identity-service:identity-contract"))
    implementation(project(":identity-service:domain"))
    implementation(libs.spring.security.crypto)
    implementation(libs.spring.security.jose)

    testImplementation(project(":kernel:kernel-test"))
}