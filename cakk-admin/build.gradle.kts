description = "admin module"

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}

dependencies {
	implementation(project(":cakk-common"))
	implementation(project(":cakk-domain:mysql"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework:spring-tx")

	// Point
	implementation("org.locationtech.jts:jts-core:1.18.2")
}
