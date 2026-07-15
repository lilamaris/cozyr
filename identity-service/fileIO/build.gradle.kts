plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":identity-service:application"))
    implementation(project(":identity-service:domain"))

    testImplementation(project(":kernel:kernel-test"))
}