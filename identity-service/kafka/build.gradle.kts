plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
    id("cozyr.spring.kafka")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":identity-service:identity-contract"))
    implementation(project(":identity-service:application"))

    testImplementation(project(":kernel:kernel-test"))
}
