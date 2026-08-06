import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("cozyr.spring.observability")
    id("cozyr.spring.gateway")
    alias(libs.plugins.spring.boot)
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}