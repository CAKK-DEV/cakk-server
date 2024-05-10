package com.cakk.api.service.user;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.factory.OidcProviderFactory;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.service.user.UserService;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.reader.UserReader;
import com.cakk.domain.repository.writer.UserWriter;

@DisplayName("유저 관련 비즈니스 로직 테스트")
class UserServiceTest extends ServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private OidcProviderFactory oidcProviderFactory;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private UserReader userReader;

	@Mock
	private UserWriter userWriter;

	@Test
	void 회원가입에_성공한다() {
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
		JwtResponse result = userService.signUp(dto);

		// then
		Assertions.assertNotNull(result.accessToken());
		Assertions.assertNotNull(result.refreshToken());
		Assertions.assertNotNull(result.grantType());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userWriter, times(1)).create(any(User.class));
		verify(jwtProvider, times(1)).generateToken(user);
	}

	@Test
	void 만료된_id_token이라면_회원가입_시_에러를_던진다() {
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
			() -> userService.signUp(dto),
			ReturnCode.EXPIRED_JWT_TOKEN.getMessage());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userWriter, times(0)).create(any(User.class));
		verify(jwtProvider, times(0)).generateToken(user);
	}

	@Test
	void 로그인에_성공한다() {
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
		JwtResponse result = userService.signIn(dto);

		// then
		Assertions.assertNotNull(result.accessToken());
		Assertions.assertNotNull(result.refreshToken());
		Assertions.assertNotNull(result.grantType());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userReader, times(1)).findByProviderId(providerId);
		verify(jwtProvider, times(1)).generateToken(user);
	}

	@Test
	void 제공자_id에_해당하는_유저가_없다면_로그인_시_에러를_던진다() {
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
			() -> userService.signIn(dto),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(oidcProviderFactory, times(1)).getProviderId(dto.provider(), dto.idToken());
		verify(userReader, times(1)).findByProviderId(providerId);
		verify(jwtProvider, times(0)).generateToken(user);
	}
}
