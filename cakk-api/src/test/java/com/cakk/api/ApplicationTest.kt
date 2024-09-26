package com.cakk.api

import io.kotest.matchers.shouldNotBe

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles

import com.cakk.api.common.annotation.TestWithDisplayName

@ActiveProfiles("test")
@SpringBootTest(properties = ["spring.profiles.active=test"])
internal class ApplicationTest {

	@Autowired
	private lateinit var context: ApplicationContext

	@TestWithDisplayName("Application이 잘 실행된다.")
	fun contextLoads() {
		context shouldNotBe null
	}
}
