plugins {
    id("cozyr.java.module")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    testImplementation(project(":kernel:kernel-test"))
}