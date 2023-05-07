import java.net.URL

plugins {
    java
    id("io.freefair.lombok") version "8.0.0-rc2"
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.openapi.generator") version "6.3.0"
    id("com.mav.buildscale") version "0.0.0-SNAPSHOT"
}

buildscale {
    uri.set(URL("http://localhost:15431"))
    publishEnabled.set(true)
    verboseEnabled.set(true)
}

group = "com.mav"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

sourceSets {
    main.configure {
        java {
            srcDirs("$buildDir/generated/src/main/java")
        }
    }
}

dependencies {
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")

    runtimeOnly("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:7.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    dependsOn("openApiGenerate")
}

tasks.openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.mav.buildscale.api")
    modelPackage.set("com.mav.buildscale.api.model")
    configOptions.set(mapOf(
            "dateLibrary" to " java8",
            "hideGenerationTimestamp" to "true",
            "interfaceOnly" to "true",
            "openApiNullable" to "false",
            "useSpringBoot3" to "true",
            "useTags" to "true"
    )
    )
}
