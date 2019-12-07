plugins {
    id("org.jetbrains.intellij") version "0.4.14"
    java
    kotlin("jvm") version "1.3.61"
}

group = "ca.rightsomegoodgames.mayacharm"
version = "3.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2019.3"
    type = "PY"
    val plugins = listOf("python")
    setPlugins(*plugins.toTypedArray())
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
        <ul>
            <li>Updated plugin project to use Gradle instead of the legacy DevKit</li>
            <li>Now Supports PyCharm 2019.3</li>
        </ul>
      """)
}