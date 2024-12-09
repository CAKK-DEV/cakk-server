description = "redis module"

dependencies {
	implementation(projects.common)

	implementation(libs.spring.boot.starter.data.redis)
	implementation(libs.spring.boot.starter.redisson)
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
