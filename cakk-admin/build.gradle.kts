description = "admin module"

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}

dependencies {
	implementation(projects.common)
	implementation(projects.persistenceMysql)
	implementation(projects.application)

	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.oauth)

	implementation(libs.spring.tx)

	// Point
	implementation(libs.jts.core)
}
