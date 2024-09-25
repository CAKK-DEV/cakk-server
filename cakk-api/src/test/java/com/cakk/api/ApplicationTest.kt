package com.cakk.api

import java.util.*

import io.kotest.matchers.shouldBe
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

	@TestWithDisplayName("Application 클래스는 TimeZone을 Asia/Seoul로 설정한다.")
	fun timeZone() {
		// Expected timezone
		val expectedTimeZone = TimeZone.getTimeZone("Asia/Seoul")

		// Actual timezone
		val actualTimeZone = TimeZone.getDefault()

		// then
		actualTimeZone shouldBe expectedTimeZone
	}
}
