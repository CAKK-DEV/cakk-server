package com.cakk.domain.facade.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.enums.Role;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.TestWithDisplayName;
import com.cakk.domain.base.FacadeTest;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;
import com.cakk.domain.mysql.facade.user.UserCommandFacade;
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository;
import com.cakk.domain.mysql.repository.jpa.UserWithdrawalJpaRepository;

class UserCommandFacadeTest extends FacadeTest {

	@InjectMocks
	private UserCommandFacade userCommandFacade;

	@Mock
	private UserJpaRepository userJpaRepository;

	@Mock
	private UserWithdrawalJpaRepository userWithdrawalJpaRepository;

	@TestWithDisplayName("유저를 생성한다")
	void create() {
		// given
		final User user = getUserFixture(Role.USER);

		// when
		userCommandFacade.create(user);

		// then
		verify(userJpaRepository, times(1)).findByProviderId(user.getProviderId());
		verify(userJpaRepository, times(1)).save(user);
	}

	@TestWithDisplayName("유저가 이미 있으면 User 생성에 실패한다")
	void create2() {
		// given
		final User user = getUserFixture(Role.USER);

		doReturn(Optional.of(user)).when(userJpaRepository).findByProviderId(user.getProviderId());

		// when
		assertThrows(
			CakkException.class,
			() -> userCommandFacade.create(user),
			ReturnCode.ALREADY_EXIST_USER.getMessage()
		);

		// then
		verify(userJpaRepository, times(1)).findByProviderId(user.getProviderId());
		verify(userJpaRepository, never()).save(user);
	}

	@TestWithDisplayName("유저 정보를 수정한다")
	void updateProfile() {
		// given
		final User user = getUserFixture(Role.USER);
		final ProfileUpdateParam param = getConstructorMonkey().giveMeBuilder(ProfileUpdateParam.class)
			.set("profileImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(100))
			.set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
			.set("gender", Arbitraries.of(Gender.class))
			.set("birthday", LocalDate.now())
			.sample();

		// when
		userCommandFacade.updateProfile(user, param);

		// then
		assertEquals(param.profileImageUrl(), user.getProfileImageUrl());
		assertEquals(param.nickname(), user.getNickname());
		assertEquals(param.email(), user.getEmail());
		assertEquals(param.gender(), user.getGender());
		assertEquals(param.birthday(), user.getBirthday());
	}

	@TestWithDisplayName("유저를 탈퇴한다")
	void withdraw() {
		// given
		final User user = getUserFixture(Role.USER);
		final UserWithdrawal withdrawal = getConstructorMonkey().giveMeBuilder(UserWithdrawal.class)
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
			.set("gender", Arbitraries.of(Gender.class))
			.set("birthday", LocalDate.now())
			.set("role", Arbitraries.of(Role.class))
			.set("withdrawalDate", LocalDateTime.now())
			.sample();

		// when
		userCommandFacade.withdraw(user, withdrawal);

		// then
		verify(userWithdrawalJpaRepository, times(1)).save(any());
		verify(userJpaRepository, times(1)).delete(user);
	}
}
