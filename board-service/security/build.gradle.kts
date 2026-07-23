plugins {
    id("cozyr.java.module")
    id("cozyr.spring.autoconfigure")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":identity-service:identity-contract"))
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.jakarta.servlet.api)

    testImplementation(project(":kernel:kernel-test"))
}