rootProject.name = "cakk"

pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}

// presentation
module(name=":app-api", "cakk-api")
module(name=":admin-api", "cakk-admin")
module(name=":batch", "cakk-batch")

// external
module(name=":external", "cakk-external")

// application
module(name=":application", "cakk-core")

// domain & persistence
module(name=":persistence", "cakk-domain")
module(name=":persistence-mysql", "cakk-domain/mysql")
module(name=":persistence-redis", "cakk-domain/redis")

// common
module(name=":common", "cakk-common")

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun module(name: String, path: String) {
	include(name)
	project(name).projectDir = file(path)
}
