plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.web")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":board-service:application"))
    implementation(project(":identity-service:identity-contract"))
}