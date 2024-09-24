package com.cakk.api.service.user;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.core.dto.response.user.ProfileInformationResponse;
import com.cakk.core.facade.user.UserManageFacade;
import com.cakk.core.facade.user.UserReadFacade;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;

@DisplayName("유저 관련 비즈니스 로직 테스트")
class UserServiceTest extends ServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserReadFacade userReadFacade;

	@Mock
	private UserManageFacade userManagerFacade;

	@TestWithDisplayName("유저 프로필을 조회한다.")
	void findProfile1() {
		// given
		final User user = getUser();

		doReturn(user).when(userReadFacade).findByUserId(user.getId());

		// when
		final ProfileInformationResponse response = userService.findProfile(user);

		// then
		Assertions.assertNotNull(response);

		verify(userReadFacade, times(1)).findByUserId(user.getId());
	}

	@TestWithDisplayName("유저가 존재하지 않으면 유저 프로필 조회에 실패한다.")
	void findProfile2() {
		// given
		final User user = getConstructorMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();

		doThrow(new CakkException(ReturnCode.NOT_EXIST_USER)).when(userReadFacade).findByUserId(user.getId());

		// when
		Assertions.assertThrows(CakkException.class,
			() -> userService.findProfile(user),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(userReadFacade, times(1)).findByUserId(user.getId());
	}

	@TestWithDisplayName("유저 프로필을 수정한다.")
	void updateInformation() {
		// given
		final User user = getConstructorMonkey().giveMeBuilder(User.class)
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();
		ReflectionTestUtils.setField(user, "id", 1L);
		final ProfileUpdateParam param = getConstructorMonkey().giveMeBuilder(ProfileUpdateParam.class)
			.setNotNull("profileImageUrl")
			.setNotNull("nickname")
			.setNotNull("email")
			.setNotNull("gender")
			.setNotNull("birthday")
			.set("user", user)
			.sample();

		// when & then
		Assertions.assertDoesNotThrow(() -> userService.updateInformation(param));
	}

	@TestWithDisplayName("유저를 탈퇴한다.")
	void withdraw1() {
		// given
		final User user = getUser();

		doReturn(user).when(userReadFacade).findByIdWithAll(user.getId());

		// when & then
		Assertions.assertDoesNotThrow(() -> userService.withdraw(user));

		verify(userReadFacade, times(1)).findByIdWithAll(user.getId());
		verify(userManagerFacade, times(1)).withdraw(any(), any());
	}

	@TestWithDisplayName("유저가 없는 경우, 탈퇴에 실패한다.")
	void withdraw2() {
		// given
		final User user = getUser();

		doThrow(new CakkException(ReturnCode.NOT_EXIST_USER)).when(userReadFacade).findByIdWithAll(user.getId());

		// when & then
		Assertions.assertThrows(CakkException.class,
			() -> userService.withdraw(user),
			ReturnCode.NOT_EXIST_USER.getMessage());

		verify(userReadFacade, times(1)).findByIdWithAll(user.getId());
		verify(userManagerFacade, never()).withdraw(any(), any());
	}
}
