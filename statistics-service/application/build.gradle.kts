plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.autoconfigure")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-application"))
    implementation(project(":statistics-service:domain"))

    testImplementation(project(":kernel:kernel-test"))
}