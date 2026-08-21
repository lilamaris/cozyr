pluginManagement {
    repositories {
        includeBuild("build-logic")
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
    }
}

rootProject.name = "cozyr"

include("kernel:kernel-core")
include("kernel:kernel-test")
include("kernel:kernel-application")
include("kernel:kernel-web")
include("kernel:kernel-security")
include("kernel:kernel-message")

include("gateway")

include("board-service:board-contract")
include("board-service:domain")
include("board-service:application")
include("board-service:security")
include("board-service:jpa")
include("board-service:launcher")
include("board-service:web")
include("board-service:kafka")

include("identity-service:identity-contract")
include("identity-service:identity-resource-server")
include("identity-service:domain")
include("identity-service:application")
include("identity-service:security")
include("identity-service:jpa")
include("identity-service:fileIO")
include("identity-service:launcher")
include("identity-service:web")
include("identity-service:kafka")

include("statistics-service:domain")
include("statistics-service:application")
include("statistics-service:security")
include("statistics-service:jpa")
include("statistics-service:launcher")
include("statistics-service:web")
include("statistics-service:kafka")

include("reservation-service:domain")
