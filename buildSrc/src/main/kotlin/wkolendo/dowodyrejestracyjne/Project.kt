import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.kotlin

const val GRADLE_PLUGIN_VERSION = "7.0.2"

const val KOTLIN_VERSION = "1.5.31"

fun ScriptHandlerScope.setup() {
    repositories.addDefaults()
    dependencies.apply {
        classpath("com.android.tools.build:gradle:$GRADLE_PLUGIN_VERSION")
        classpath(kotlin("gradle-plugin", KOTLIN_VERSION))
    }
}

fun Project.addRepositories() {
    repositories.addDefaults().apply {
        maven { url = project.uri("https://jitpack.io") }
        maven { url = project.uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

private fun RepositoryHandler.addDefaults() = apply {
    google()
    mavenCentral()
}