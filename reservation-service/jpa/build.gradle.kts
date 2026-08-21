plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.postgresql")
    id("cozyr.spring.h2")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":kernel:kernel-core"))
    implementation(project(":reservation-service:domain"))
    implementation(project(":reservation-service:application"))
    implementation(libs.spring.boot.starter.data.jpa)

    testImplementation(libs.spring.boot.starter.data.jpa.test)
}
