plugins {
    id("cozyr.java.module")
    id("cozyr.spring.platform")
    `java-library`
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    compileOnlyApi(libs.jakarta.persistence.api)

    testImplementation(project(":kernel:kernel-test"))
}
