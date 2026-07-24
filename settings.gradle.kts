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

include("gateway")

include("board-service:domain")
include("board-service:application")
include("board-service:security")
include("board-service:jpa")
include("board-service:launcher")
include("board-service:web")

include("identity-service:identity-contract")
include("identity-service:domain")
include("identity-service:application")
include("identity-service:security")
include("identity-service:jpa")
include("identity-service:fileIO")
include("identity-service:launcher")
include("identity-service:web")
include("identity-service:kafka")