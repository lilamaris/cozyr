import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.observability")
    id("cozyr.spring.autoconfigure")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":board-service:application"))
    implementation(project(":board-service:domain"))
    implementation(project(":board-service:jpa"))
    implementation(project(":board-service:web"))
    implementation(project(":board-service:security"))
    implementation(project(":board-service:kafka"))
}

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}