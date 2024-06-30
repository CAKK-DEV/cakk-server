package com.cakk.api.common.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(
	properties = "spring.profiles.active=test",
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class IntegrationTest {

	@Autowired
	protected TestRestTemplate restTemplate;

	@LocalServerPort
	protected int port;

	@Autowired
	protected ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	protected UserReader userReader;

	protected static final String BASE_URL = "http://localhost:";

	protected final FixtureMonkey getConstructorMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected HttpHeaders getAuthHeader() {
		final HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthToken().accessToken());

		return headers;
	}

	protected HttpHeaders getAuthHeaderById(Long id) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthTokenById(id).accessToken());

		return headers;
	}

	protected JsonWebToken getAuthToken() {
		final User user = userReader.findByUserId(1L);

		return jwtProvider.generateToken(user);
	}

	private JsonWebToken getAuthTokenById(Long id) {
		final User user = userReader.findByUserId(id);

		return jwtProvider.generateToken(user);
	}

	protected Long getUserId() {
		return 1L;
	}

	protected long getRefreshTokenExpiredSecond() {
		return jwtProvider.getRefreshTokenExpiredSecond();
	}
}
