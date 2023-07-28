import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlinx.kover") version "0.7.3"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.1")
}

koverReport {
    filters {
        includes {
            classes("org.example.*")
        }
    }
    defaults {
        xml {
            onCheck = false
            setReportFile(layout.projectDirectory.file(".qodana/code-coverage/result.xml").asFile)
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.koverXmlReport)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}