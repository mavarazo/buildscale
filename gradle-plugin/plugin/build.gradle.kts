plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "com.mav.buildscale"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.mockito:mockito-core:5.1.1")
}

gradlePlugin {
    website.set("https://github.com/mavarazo/buildscale-plugin")
    vcsUrl.set("https://github.com/mavarazo/buildscale-plugin.git")
    val buildscale by plugins.creating {
        id = "com.mav.buildscale"
        implementationClass = "com.mav.buildscale.plugin.BuildscalePlugin"
        displayName = "Gradle plugin to collect data about builds"
        description = "Collects data about the system, finished tasks and tests and publish them to the Buildscale server."
        tags.addAll("build")
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
