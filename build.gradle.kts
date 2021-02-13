import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
    id("org.jetbrains.intellij") version "0.6.5"
    id("org.jetbrains.changelog") version "1.1.1"
}

val pluginGroup: String by project
val pluginName_: String by project
val pluginVersion: String by project

val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
}

val publishToken: String? = System.getenv("IntellijPublishToken")

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName = pluginName_
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    setPlugins("python") // TODO pull from properties file
}

changelog {
    version = pluginVersion
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    runPluginVerifier {
        ideVersions(pluginVerifierIdeVersions)
    }
}
