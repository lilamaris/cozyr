plugins {
    id("cozyr.java.module")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-message"))
    api(project(":identity-service:identity-contract"))

    testImplementation(project(":kernel:kernel-test"))
}
