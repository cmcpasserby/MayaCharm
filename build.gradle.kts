import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    id("org.jetbrains.intellij") version "0.7.2"
    id("org.jetbrains.changelog") version "1.1.2"
}

val pluginGroup: String by project
val pluginName_: String by project
val pluginVersion: String by project

val pluginSinceVersion: String by project
val pluginUntilVersion: String by project
val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project
val platformPlugins: String by project

val publishToken: String? = System.getenv("IDEA_PLATFORM_PUBLISH")

group = pluginGroup
version = pluginVersion

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName = pluginName_
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    setPlugins(*platformPlugins.split(",").map(String::trim).filter(String::isNotEmpty).toTypedArray())
}

changelog {
    version = pluginVersion
    groups = emptyList()
}

tasks {
    withType<JavaCompile> {
        options.release.set(8)
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceVersion)
        untilBuild(null)

        pluginDescription(
            closure {
                File(projectDir, "README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
            }
        )

        changeNotes(
            closure {
                changelog.get(pluginVersion).toHTML()
            }
        )
    }

    runPluginVerifier {
        ideVersions(pluginVerifierIdeVersions)
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(publishToken)
        channels(pluginVersion.split('-').getOrElse(1) {"default"}.split('.').first())
    }
}
