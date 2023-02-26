val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("maven-publish")
}

buildscript {
    repositories { mavenCentral() }

    dependencies {
        val kotlinVersion = "1.8.10"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

group = "com.myfood"
version = "0.3.2"
application {
    mainClass.set("com.myfood.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")

    implementation("mysql:mysql-connector-java:8.0.19")
    implementation("com.zaxxer:HikariCP:3.4.2")

    implementation("org.xerial:sqlite-jdbc:3.30.1")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    implementation("io.ktor:ktor-server-default-headers:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
}

tasks {
    create("stage").dependsOn("installDist")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "com.myfood"
            artifactId = "myfood-ktor-server"
            version = "0.3.2"

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}