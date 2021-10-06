import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addAll() {
    addKotlin()
    addLifecycle()
    addAndroidX()
    addGoogle()
    addOthers()
}

private fun DependencyHandler.addKotlin() {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KOTLIN_VERSION}")
    implementation("org.jetbrains.kotlin:kotlin-android-extensions-runtime:${KOTLIN_VERSION}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}

private fun DependencyHandler.addLifecycle() {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-rc01")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-rc01")
    implementation("androidx.lifecycle:lifecycle-process:2.4.0-rc01")
}

private fun DependencyHandler.addAndroidX() {
    implementation("androidx.annotation:annotation:1.3.0-beta01")
    implementation("androidx.appcompat:appcompat:1.4.0-alpha03")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.core:core-ktx:1.7.0-beta02")
    implementation("androidx.fragment:fragment-ktx:1.4.0-alpha10")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
}

private fun DependencyHandler.addGoogle() {
    implementation("com.google.android.material:material:1.5.0-alpha04")
}

private fun DependencyHandler.addOthers() {
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.journeyapps:zxing-android-embedded:3.6.0")
    implementation("com.google.zxing:core:3.3.3")
}

private fun DependencyHandler.implementation(notation: Any) = add("implementation", notation)

private fun DependencyHandler.compileOnly(notation: Any) = add("compileOnly", notation)

private fun DependencyHandler.debugCompile(notation: Any) = add("debugCompile", notation)

private fun DependencyHandler.kapt(notation: Any) = add("kapt", notation)