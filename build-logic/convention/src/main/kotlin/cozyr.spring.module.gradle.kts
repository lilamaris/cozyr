import convention.libs
import convention.requireLibrary

plugins {
    id("cozyr.java.module")
    id("cozyr.spring.platform")
}

dependencies {
    implementation(libs.requireLibrary("spring-tx"))
    implementation(libs.requireLibrary("spring-context"))
}