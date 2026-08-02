plugins {
    id("cozyr.java.module")
    id("cozyr.spring.web")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":kernel:kernel-core"))
    api(project(":kernel:kernel-application"))

    testImplementation(project(":kernel:kernel-test"))
}