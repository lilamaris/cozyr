import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.spring.platform")
}

dependencies {
    runtimeOnly(libs.requireLibrary("h2database"))
}