plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.security")
    id("cozyr.spring.autoconfigure")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity-service:identity-contract"))
    implementation(project(":kernel:kernel-core"))
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.jakarta.servlet.api)

    testImplementation(project(":kernel:kernel-test"))
}
