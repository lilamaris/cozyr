plugins {
    id("cozyr.spring.module")
}

group = "com.lilamaris.cozyr"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":identity-service:identity-contract"))
    implementation(project(":board-service:board-contract"))
    implementation(project(":reservation-service:reservation-contract"))
    implementation(project(":statistics-service:statistics-contract"))

    implementation(project(":identity-service:application"))
}
