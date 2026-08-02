plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.web")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-web"))
    implementation(project(":board-service:application"))
    implementation(project(":board-service:domain"))
    implementation(project(":identity-service:identity-contract"))
}