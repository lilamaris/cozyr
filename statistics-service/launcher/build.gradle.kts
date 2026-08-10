import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("cozyr.spring.module")
    id("cozyr.spring.observability")
    id("cozyr.spring.autoconfigure")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":statistics-service:application"))
    implementation(project(":statistics-service:domain"))
    implementation(project(":statistics-service:jpa"))
    implementation(project(":statistics-service:kafka"))
}

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}