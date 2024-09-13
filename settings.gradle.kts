rootProject.name = "cakk"

include(
	"cakk-api",
	"cakk-admin",
	"cakk-batch",
	"cakk-core",
	"cakk-domain:mysql",
	"cakk-domain:redis",
	"cakk-external",
	"cakk-common"
)

pluginManagement {
	val kotlinVersion: String by settings
	val springBootVersion: String by settings
	val springDependencyManagementVersion: String by settings
	val ktlintVersion: String by settings

	resolutionStrategy {
		eachPlugin {
			when (requested.id.id) {
				"org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
				"org.springframework.boot" -> useVersion(springBootVersion)
				"io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
			}
		}
	}
}
