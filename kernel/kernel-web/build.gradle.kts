plugins {
    id("cozyr.java.module")
    id("cozyr.spring.platform")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-core"))
    api(project(":kernel:kernel-application"))
    api(libs.spring.web)
    api(libs.jakarta.servlet.api)
    api(libs.tools.jackson.databind)

    testImplementation(project(":kernel:kernel-test"))
}