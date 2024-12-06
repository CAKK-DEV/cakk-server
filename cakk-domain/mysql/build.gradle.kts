description = "mysql module"

dependencies {
	implementation(projects.common)

    // jpa
    api(libs.spring.boot.starter.data.jpa)

	// hibernate
	implementation(libs.hibernate.core)
	implementation(libs.hibernate.spatial)

    // querydsl
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-spatial")
	annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
	annotationProcessor(libs.jakarta.annotation.api)
	annotationProcessor(libs.jakarta.persistence.api)

    // database
    runtimeOnly(libs.mysql.connector.java)

	//serialize
	implementation(libs.jackson.datatype.jsr310)
	implementation(libs.jackson.databind)

	// log
	implementation(libs.h6spy)

	// test
	testImplementation(libs.assertj.core)
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.junit.jupiter)
	testImplementation(libs.kotest.junit)
	testImplementation(libs.fixture.monkey.starter)
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}

tasks.test {
	testLogging {
		showStandardStreams = true
		showCauses = true
		showExceptions = true
		showStackTraces = true
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}
}
