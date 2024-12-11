description = "domain module"

dependencies {
	implementation(projects.common)
	implementation(projects.persistence)

	implementation(libs.spring.data.commons)
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}
