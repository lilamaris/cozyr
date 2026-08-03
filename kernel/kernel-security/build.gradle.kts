plugins {
    id("cozyr.java.module")
    id("cozyr.spring.platform")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-web"))
    api(libs.jakarta.servlet.api)
    api(libs.spring.security.core)
    api(libs.spring.security.web)

    testImplementation(project(":kernel:kernel-test"))
}
