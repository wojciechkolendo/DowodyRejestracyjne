plugins {
	id("com.android.application")
	kotlin("android")
	kotlin("kapt")
	kotlin("android.extensions")
}

android {
	compileSdk = App.COMPILE_SDK
	defaultConfig {
		applicationId = App.ID
		minSdk = App.MIN_SDK
		targetSdk = App.TARGET_SDK
		versionCode = App.CODE
		versionName = App.VERSION
		flavorDimensions.add(App.FLAVOR_DIMENSION)
	}

	buildFeatures.dataBinding = true

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_1_8.toString()
		freeCompilerArgs = listOf(
				"-Xopt-in=kotlin.contracts.ExperimentalContracts",
				"-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
				"-Xopt-in=kotlin.ExperimentalStdlibApi",
				"-Xopt-in=kotlin.experimental.ExperimentalTypeInference"
		)
	}

	applicationVariants.all {
		outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }.forEach { it.outputFileName = it.outputFileName.setupName() }
	}
}

androidExtensions.isExperimental = true

dependencies.addAll()