pluginManagement {
    repositories {
        includeBuild("build-logic")
        gradlePluginPortal()
        mavenCentral()
    }
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

include("board-service:domain")
include("board-service:application")
include("board-service:jpa")
include("board-service:launcher")
include("board-service:web")