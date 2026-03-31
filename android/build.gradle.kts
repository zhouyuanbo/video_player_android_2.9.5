import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "io.flutter.plugins.videoplayer"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "2.3.0"
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.13.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}
apply(plugin = "com.android.library")
apply(plugin = "kotlin-android")

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension>("kotlin") {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(JavaVersion.VERSION_17.toString()))
    }
}
extensions.configure<LibraryExtension>("android") {
    namespace = "io.flutter.plugins.videoplayer"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        checkAllWarnings = true
        warningsAsErrors = true
        disable.addAll(setOf("AndroidGradlePluginVersion", "InvalidPackage", "GradleDependency", "NewerVersionAvailable"))
        baseline = file("lint-baseline.xml")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        val exoplayerVersion = "1.9.2"
        add("implementation", "androidx.media3:media3-exoplayer:${exoplayerVersion}")
        add("implementation", "androidx.media3:media3-exoplayer-hls:${exoplayerVersion}")
        add("implementation", "androidx.media3:media3-exoplayer-dash:${exoplayerVersion}")
        add("implementation", "androidx.media3:media3-exoplayer-rtsp:${exoplayerVersion}")
        add("implementation", "androidx.media3:media3-exoplayer-smoothstreaming:${exoplayerVersion}")
        add("testImplementation", "junit:junit:4.13.2")
        add("testImplementation", "androidx.test:core:1.7.0")
        add("testImplementation", "org.mockito:mockito-core:5.23.0")
        add("testImplementation", "org.robolectric:robolectric:4.16")
        add("testImplementation", "androidx.media3:media3-test-utils:${exoplayerVersion}")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.outputs.upToDateWhen { false }
                it.testLogging {
                    events("passed", "skipped", "failed", "standardOut", "standardError")
                    showStandardStreams = true
                }
                // The org.gradle.jvmargs property that may be set in gradle.properties does not impact
                // the Java heap size when running the Android unit tests. The following property here
                // sets the heap size to a size large enough to run the robolectric tests across
                // multiple SDK levels.
                it.jvmArgs("-Xmx4G")
            }
        }
    }
}
