package com.cakk.api.provider;

import static com.cakk.api.common.utils.TestUtils.*;
import static com.cakk.common.enums.ReturnCode.*;
import static com.cakk.common.enums.Role.*;

import java.security.Key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ProviderTest;
import com.cakk.api.config.JwtConfig;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.redis.repository.TokenRedisRepository;

@Import(JwtConfig.class)
@DisplayName("Jwt Provider 테스트")
class JwtProviderTest extends ProviderTest {

	@InjectMocks
	private JwtProvider jwtProvider;

	@Mock
	private Key key;

	@Mock
	private TokenRedisRepository tokenRedisRepository;

	@Value("${jwt.secret}")
	private String secretKey;

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(jwtProvider, "accessTokenExpiredSecond", ACCESS_TOKEN_EXPIRED_SECOND);
		ReflectionTestUtils.setField(jwtProvider, "refreshTokenExpiredSecond", REFRESH_TOKEN_EXPIRED_SECOND);
		ReflectionTestUtils.setField(jwtProvider, "grantType", GRANT_TYPE);
		ReflectionTestUtils.setField(jwtProvider, "userKey", USER_KEY);
		ReflectionTestUtils.setField(jwtProvider, "key", Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)));
	}

	@TestWithDisplayName("토큰 생성에 성공한다.")
	void generateToken1() {
		// given
		User user = fixtureMonkey.giveMeOne(User.class);

		// when
		JsonWebToken jwt = jwtProvider.generateToken(user);

		// then
		Assertions.assertNotNull(jwt.accessToken());
		Assertions.assertNotNull(jwt.refreshToken());
		Assertions.assertNotNull(jwt.grantType());
	}

	@TestWithDisplayName("User가 null인 경우, 토큰 생성에 실패한다.")
	void generateToken2() {
		// given
		User user = null;

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> jwtProvider.generateToken(user),
			EMPTY_USER.getCode());
	}

	@TestWithDisplayName("액세스 토큰으로부터 인증 정보를 가져온다.")
	void getAuthentication() {
		// given
		User user = fixtureMonkey.giveMeOne(User.class);
		ReflectionTestUtils.setField(user, "id", 1L);
		ReflectionTestUtils.setField(user, "role", USER);

		String accessToken = jwtProvider.generateToken(user).accessToken();

		// when
		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		// then
		Assertions.assertEquals(authentication.getAuthorities().toString(), "[%s]".formatted(USER.getSecurityRole()));
	}

	@TestWithDisplayName("토큰으로부터 Claim 정보를 가져온다.")
	void parseClaims() {
		// given
		User user = fixtureMonkey.giveMeOne(User.class);
		ReflectionTestUtils.setField(user, "id", 1L);
		ReflectionTestUtils.setField(user, "role", USER);

		String accessToken = jwtProvider.generateToken(user).accessToken();

		// when
		Claims claims = jwtProvider.parseClaims(accessToken);

		// then
		Assertions.assertNotNull(claims);
	}
}
