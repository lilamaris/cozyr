plugins {
    id("cozyr.java.module")
    id("cozyr.spring.security")
    id("cozyr.spring.autoconfigure")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-security"))
    implementation(project(":identity-service:identity-resource-server"))

    testImplementation(project(":kernel:kernel-test"))
}
