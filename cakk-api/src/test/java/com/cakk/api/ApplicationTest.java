package com.cakk.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.cakk.api.common.annotation.TestWithDisplayName;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.profiles.active=test")
class ApplicationTest {

	@Autowired
	private ApplicationContext context;

	@TestWithDisplayName("Application이 잘 실행된다.")
	void contextLoads() {
		assertNotNull(context);
	}

	@TestWithDisplayName("Application 클래스는 TimeZone을 Asia/Seoul로 설정한다.")
	void timeZone() {
		// Expected timezone
		TimeZone expectedTimeZone = TimeZone.getTimeZone("Asia/Seoul");

		// Actual timezone
		TimeZone actualTimeZone = TimeZone.getDefault();

		// then
		assertEquals(expectedTimeZone, actualTimeZone);
	}
}
