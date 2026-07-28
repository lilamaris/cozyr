plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.security")
    id("cozyr.spring.autoconfigure")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":identity-service:identity-contract"))
    api(libs.spring.boot.starter.oauth2.resource.server)
    api(libs.jakarta.servlet.api)
    implementation(project(":kernel:kernel-core"))

    testImplementation(project(":kernel:kernel-test"))
}
