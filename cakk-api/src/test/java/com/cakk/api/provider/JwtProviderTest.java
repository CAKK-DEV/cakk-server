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
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.config.JwtConfig;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.redis.repository.TokenRedisRepository;

@Import(JwtConfig.class)
@DisplayName("Jwt Provider 테스트")
class JwtProviderTest extends MockitoTest {

	private JwtProvider jwtProvider;

	@Mock
	private TokenRedisRepository tokenRedisRepository;

	@Value("${jwt.secret}")
	private String secretKey;

	@BeforeEach
	void setup() {
		jwtProvider = new JwtProvider(
			Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)),
			tokenRedisRepository,
			ACCESS_TOKEN_EXPIRED_SECOND,
			REFRESH_TOKEN_EXPIRED_SECOND,
			GRANT_TYPE,
			USER_KEY
		);
	}

	@TestWithDisplayName("토큰 생성에 성공한다.")
	void generateToken1() {
		// given
		User user = getConstructorMonkey().giveMeOne(User.class);

		// when
		JsonWebToken jwt = jwtProvider.generateToken(user);

		// then
		Assertions.assertNotNull(jwt.getAccessToken());
		Assertions.assertNotNull(jwt.getRefreshToken());
		Assertions.assertNotNull(jwt.getGrantType());
	}

	@TestWithDisplayName("액세스 토큰으로부터 인증 정보를 가져온다.")
	void getAuthentication() {
		// given
		User user = getConstructorMonkey().giveMeOne(User.class);
		ReflectionTestUtils.setField(user, "id", 1L);
		ReflectionTestUtils.setField(user, "role", USER);

		String accessToken = jwtProvider.generateToken(user).getAccessToken();

		// when
		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		// then
		Assertions.assertEquals(authentication.getAuthorities().toString(), "[%s]".formatted(USER.getSecurityRole()));
	}

	@TestWithDisplayName("토큰으로부터 Claim 정보를 가져온다.")
	void parseClaims() {
		// given
		User user = getConstructorMonkey().giveMeOne(User.class);
		ReflectionTestUtils.setField(user, "id", 1L);
		ReflectionTestUtils.setField(user, "role", USER);

		String accessToken = jwtProvider.generateToken(user).getAccessToken();

		// when
		Claims claims = jwtProvider.parseClaims(accessToken);

		// then
		Assertions.assertNotNull(claims);
	}
}
