import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.observability")
    id("cozyr.spring.autoconfigure")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":reservation-service:application"))
    implementation(project(":reservation-service:domain"))
    implementation(project(":reservation-service:jpa"))
    implementation(project(":reservation-service:web"))
    implementation(project(":reservation-service:security"))
}

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}
