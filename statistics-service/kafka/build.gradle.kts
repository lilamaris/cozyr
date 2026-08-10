plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
    id("cozyr.spring.kafka")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":statistics-service:application"))
    implementation(project(":board-service:board-contract"))
    implementation(libs.tools.jackson.databind)

    testImplementation(project(":kernel:kernel-test"))
}
