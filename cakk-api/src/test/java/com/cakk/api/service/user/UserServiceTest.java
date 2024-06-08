package com.cakk.api.service.user;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.BusinessInformationWriter;
import com.cakk.domain.mysql.repository.writer.CakeLikeWriter;
import com.cakk.domain.mysql.repository.writer.CakeShopLikeWriter;
import com.cakk.domain.mysql.repository.writer.UserWriter;

@DisplayName("유저 관련 비즈니스 로직 테스트")
class UserServiceTest extends ServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserReader userReader;

	@Mock
	private UserWriter userWriter;

	@Mock
	private CakeShopLikeWriter cakeShopLikeWriter;

	@Mock
	private CakeLikeWriter cakeLikeWriter;

	@Mock
	private BusinessInformationWriter businessInformationWriter;

	@TestWithDisplayName("유저 프로필을 조회한다.")
	void findProfile1() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();

		doReturn(user).when(userReader).findByUserId(user.getId());

		// when
		final ProfileInformationResponse response = userService.findProfile(user);

		// then
		Assertions.assertNotNull(response);

		verify(userReader, times(1)).findByUserId(user.getId());
	}

	@TestWithDisplayName("유저가 존재하지 않으면 유저 프로필 조회에 실패한다.")
	void findProfile2() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();

		doThrow(new CakkException(ReturnCode.NOT_EXIST_USER)).when(userReader).findByUserId(user.getId());

		// when
		Assertions.assertThrows(CakkException.class,
			() -> userService.findProfile(user),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(userReader, times(1)).findByUserId(user.getId());
	}

	@TestWithDisplayName("유저 프로필을 수정한다.")
	void updateInformation() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();
		final ProfileUpdateRequest request = getConstructorMonkey().giveMeOne(ProfileUpdateRequest.class);

		doReturn(user).when(userReader).findByUserId(user.getId());

		// when & then
		Assertions.assertDoesNotThrow(() -> userService.updateInformation(user, request));

		verify(userReader, times(1)).findByUserId(user.getId());
	}

	@TestWithDisplayName("유저를 탈퇴한다.")
	void withdraw1() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();

		doReturn(user).when(userReader).findByUserId(user.getId());

		// when & then
		Assertions.assertDoesNotThrow(() -> userService.withdraw(user));

		verify(userReader, times(1)).findByUserId(user.getId());
		verify(cakeLikeWriter, times(1)).deleteAllByUser(user);
		verify(cakeShopLikeWriter, times(1)).deleteAllByUser(user);
		verify(businessInformationWriter, times(1)).deleteAllByUser(user);
		verify(userWriter, times(1)).delete(any(), any());
	}

	@TestWithDisplayName("유저가 없는 경우, 탈퇴에 실패한다.")
	void withdraw2() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();

		doThrow(new CakkException(ReturnCode.NOT_EXIST_USER)).when(userReader).findByUserId(user.getId());

		// when & then
		Assertions.assertThrows(CakkException.class,
			() -> userService.withdraw(user),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(userReader, times(1)).findByUserId(user.getId());
		verify(cakeLikeWriter, times(0)).deleteAllByUser(user);
		verify(cakeShopLikeWriter, times(0)).deleteAllByUser(user);
		verify(businessInformationWriter, times(0)).deleteAllByUser(user);
		verify(userWriter, times(0)).delete(any(), any());
	}
}
