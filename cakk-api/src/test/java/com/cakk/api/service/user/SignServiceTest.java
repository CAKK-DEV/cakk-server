package com.cakk.api.service.user;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.factory.OidcProviderFactory;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.UserWriter;
import com.cakk.domain.redis.repository.TokenRedisRepository;

@DisplayName("Sign 관련 비즈니스 로직 테스트")
class SignServiceTest extends ServiceTest {

	@InjectMocks
	private SignService signService;

	@Mock
	private OidcProviderFactory oidcProviderFactory;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private UserReader userReader;

	@Mock
	private UserWriter userWriter;

	@Mock
	private TokenRedisRepository tokenRedisRepository;

	@TestWithDisplayName("회원가입에 성공한다")
	void signUp1() {
		// given
		UserSignUpRequest dto = getConstructorMonkey().giveMeOne(UserSignUpRequest.class);
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", dto.provider())
			.set("providerId", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.sample();
		JsonWebToken jwt = getConstructorMonkey().giveMeBuilder(JsonWebToken.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(user.getProviderId()).when(oidcProviderFactory).getProviderId(dto.provider(), dto.idToken());
		doReturn(user).when(userWriter).create(any(User.class));
		doReturn(jwt).when(jwtProvider).generateToken(user);

		// when
		JwtResponse result = signService.signUp(dto);

		// then
		Assertions.assertNotNull(result.accessToken());
		Assertions.assertNotNull(result.refreshToken());
		Assertions.assertNotNull(result.grantType());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userWriter, times(1)).create(any(User.class));
		verify(jwtProvider, times(1)).generateToken(user);
	}

	@TestWithDisplayName("만료된 id token이라면 회원가입 시 에러를 던진다")
	void signUp2() {
		// given
		UserSignUpRequest dto = getConstructorMonkey().giveMeOne(UserSignUpRequest.class);
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", dto.provider())
			.set("providerId", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.sample();

		doThrow(new CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).when(oidcProviderFactory).getProviderId(dto.provider(), dto.idToken());

		// then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signUp(dto),
			ReturnCode.EXPIRED_JWT_TOKEN.getMessage());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userWriter, times(0)).create(any(User.class));
		verify(jwtProvider, times(0)).generateToken(user);
	}

	@TestWithDisplayName("로그인에 성공한다.")
	void signIn() {
		// given
		UserSignInRequest dto = getConstructorMonkey().giveMeOne(UserSignInRequest.class);
		String providerId = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", dto.provider())
			.set("providerId", providerId)
			.sample();
		JsonWebToken jwt = getConstructorMonkey().giveMeBuilder(JsonWebToken.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(providerId).when(oidcProviderFactory).getProviderId(dto.provider(), dto.idToken());
		doReturn(user).when(userReader).findByProviderId(providerId);
		doReturn(jwt).when(jwtProvider).generateToken(user);

		// when
		JwtResponse result = signService.signIn(dto);

		// then
		Assertions.assertNotNull(result.accessToken());
		Assertions.assertNotNull(result.refreshToken());
		Assertions.assertNotNull(result.grantType());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userReader, times(1)).findByProviderId(providerId);
		verify(jwtProvider, times(1)).generateToken(user);
	}

	@TestWithDisplayName("제공자 id에 해당하는 유저가 없다면 로그인 시 에러를 던진다")
	void signIn2() {
		// given
		UserSignInRequest dto = getConstructorMonkey().giveMeOne(UserSignInRequest.class);
		String providerId = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", dto.provider())
			.set("providerId", providerId)
			.sample();

		doReturn(providerId).when(oidcProviderFactory).getProviderId(dto.provider(), dto.idToken());
		doThrow(new CakkException(ReturnCode.NOT_EXIST_USER)).when(userReader).findByProviderId(providerId);

		// when
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signIn(dto),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userReader, times(1)).findByProviderId(providerId);
		verify(jwtProvider, times(0)).generateToken(user);
	}

	@TestWithDisplayName("토큰 재발급에 성공한다")
	void recreateToken() {
		// given
		final String refreshToken = "refresh token";
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("providerId", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.set("createdAt", LocalDateTime.now())
			.set("updatedAt", LocalDateTime.now())
			.sample();
		JsonWebToken jwt = getConstructorMonkey().giveMeBuilder(JsonWebToken.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(false).when(tokenRedisRepository).isBlackListToken(refreshToken);
		doReturn(11111L).when(jwtProvider).getTokenExpiredSecond(refreshToken);
		doReturn(user).when(jwtProvider).getUser(refreshToken);
		doReturn(jwt).when(jwtProvider).generateToken(user);

		// when
		JwtResponse result = signService.recreateToken(refreshToken);

		// then
		Assertions.assertNotNull(result.accessToken());
		Assertions.assertNotNull(result.refreshToken());
		Assertions.assertNotNull(result.grantType());

		verify(tokenRedisRepository, times(1)).isBlackListToken(refreshToken);
		verify(jwtProvider, times(1)).getUser(refreshToken);
		verify(jwtProvider, times(1)).generateToken(user);
	}

	@TestWithDisplayName("리프레시 토큰이 만료된 경우, 토큰 재발급에 실패한다")
	void recreateToken2() {
		// given
		final String refreshToken = "refresh token";

		doReturn(false).when(tokenRedisRepository).isBlackListToken(refreshToken);
		doThrow(new CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).when(jwtProvider).getUser(refreshToken);

		// then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.recreateToken(refreshToken),
			ReturnCode.EXPIRED_JWT_TOKEN.getMessage());

		verify(jwtProvider, times(1)).getUser(refreshToken);
		verify(jwtProvider, times(0)).generateToken(any());
	}

	@TestWithDisplayName("리프레시 토큰에 null인 유저가 담겨있는 경우, 토큰 재발급에 실패한다")
	void recreateToken3() {
		// given
		final String refreshToken = "refresh token";

		doReturn(false).when(tokenRedisRepository).isBlackListToken(refreshToken);
		doThrow(new CakkException(ReturnCode.EMPTY_USER)).when(jwtProvider).getUser(refreshToken);

		// then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.recreateToken(refreshToken),
			ReturnCode.EMPTY_USER.getMessage());

		verify(jwtProvider, times(1)).getUser(refreshToken);
		verify(jwtProvider, times(0)).generateToken(any());
	}

	@TestWithDisplayName("리프레시 토큰이 블랙리스트에 있는 경우, 토큰 재발급에 실패한다")
	void recreateToken4() {
		// given
		final String refreshToken = "refresh token";

		doReturn(true).when(tokenRedisRepository).isBlackListToken(refreshToken);

		// then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.recreateToken(refreshToken),
			ReturnCode.BLACK_LIST_TOKEN.getMessage());

		verify(tokenRedisRepository, times(1)).isBlackListToken(refreshToken);
		verify(jwtProvider, times(0)).getUser(refreshToken);
	}

	@TestWithDisplayName("로그아웃에 성공한다")
	void signOut() {
		// given
		final String accessToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();
		final String refreshToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();

		doReturn(11111111111111L).when(jwtProvider).getTokenExpiredSecond(accessToken);
		doReturn(11111111111111L).when(jwtProvider).getTokenExpiredSecond(refreshToken);
		doNothing().when(tokenRedisRepository).registerBlackList(anyString(), anyLong());

		// when
		signService.signOut(accessToken, refreshToken);

		// then
		verify(jwtProvider, times(2)).getTokenExpiredSecond(anyString());
		verify(tokenRedisRepository, times(2)).registerBlackList(anyString(), anyLong());
	}

	@TestWithDisplayName("만료된 액세스 토큰일 경우 로그아웃에 실패한다")
	void signOut2() {
		// given
		final String accessToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();

		doThrow(new CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).when(jwtProvider).getTokenExpiredSecond(accessToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signOut(accessToken, null),
			ReturnCode.EXPIRED_JWT_TOKEN.getMessage());

		verify(jwtProvider, times(1)).getTokenExpiredSecond(anyString());
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong());
	}

	@TestWithDisplayName("만료된 리프레시 토큰일 경우 로그아웃에 실패한다")
	void signOut3() {
		// given
		final String accessToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();
		final String refreshToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();

		doReturn(11111111111111L).when(jwtProvider).getTokenExpiredSecond(accessToken);
		doThrow(new CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).when(jwtProvider).getTokenExpiredSecond(refreshToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signOut(accessToken, refreshToken),
			ReturnCode.EXPIRED_JWT_TOKEN.getMessage());

		verify(jwtProvider, times(2)).getTokenExpiredSecond(anyString());
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong());
	}

	@TestWithDisplayName("유효하지 않은 액세스 토큰일 경우 로그아웃에 실패한다")
	void signOut4() {
		// given
		final String accessToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();

		doThrow(new CakkException(ReturnCode.WRONG_JWT_TOKEN)).when(jwtProvider).getTokenExpiredSecond(accessToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signOut(accessToken, null),
			ReturnCode.WRONG_JWT_TOKEN.getMessage());

		verify(jwtProvider, times(1)).getTokenExpiredSecond(anyString());
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong());
	}

	@TestWithDisplayName("유효하지 않은 리프레시 토큰일 경우 로그아웃에 실패한다")
	void signOut5() {
		// given
		final String accessToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();
		final String refreshToken = Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20).sample();

		doReturn(11111111111111L).when(jwtProvider).getTokenExpiredSecond(accessToken);
		doThrow(new CakkException(ReturnCode.WRONG_JWT_TOKEN)).when(jwtProvider).getTokenExpiredSecond(refreshToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> signService.signOut(accessToken, refreshToken),
			ReturnCode.WRONG_JWT_TOKEN.getMessage());

		verify(jwtProvider, times(2)).getTokenExpiredSecond(anyString());
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong());
	}
}
