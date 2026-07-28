plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
    id("cozyr.spring.kafka")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":board-service:application"))
    implementation(project(":identity-service:identity-contract"))

    testImplementation(project(":kernel:kernel-test"))
}
