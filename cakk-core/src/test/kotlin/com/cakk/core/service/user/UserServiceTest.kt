package com.cakk.core.service.user

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.Role
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.supplyUserWithdrawalBy
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam

@DisplayName("유저 관련 비즈니스 로직 테스트")
internal class UserServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var userService: UserService

	@Mock
	private lateinit var userReadFacade: UserReadFacade

	@Mock
	private lateinit var userManagerFacade: UserManageFacade

	@TestWithDisplayName("유저 프로필을 조회한다.")
	fun findProfile1() {
		// given
		val user = getUserFixture(Role.USER)

		doReturn(user).`when`(userReadFacade).findByUserId(user.id)

		// when
		val response = userService.findProfile(user)

		// then
		response shouldNotBe null

		verify(userReadFacade, times(1)).findByUserId(user.id)
	}

	@TestWithDisplayName("유저가 존재하지 않으면 유저 프로필 조회에 실패한다.")
	fun findProfile2() {
		// given
		val user = getUserFixture(Role.USER)

		doThrow(CakkException(ReturnCode.NOT_EXIST_USER)).`when`(userReadFacade).findByUserId(user.id)

		// when
		val exception = shouldThrow<CakkException> { userService.findProfile(user) }

		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_USER
		verify(userReadFacade, times(1)).findByUserId(user.id)
	}

	@TestWithDisplayName("유저 프로필을 수정한다.")
	fun updateInformation() {
		// given
		val user = getUserFixture(Role.USER)
		val param = fixtureMonkey.giveMeBuilder(ProfileUpdateParam::class.java)
			.setNotNull("profileImageUrl")
			.setNotNull("nickname")
			.setNotNull("email")
			.setNotNull("gender")
			.setNotNull("birthday")
			.set("user", user)
			.sample()

		// when & then

		shouldNotThrow<Exception> { userService.updateInformation(param) }
	}

	@TestWithDisplayName("유저를 탈퇴한다.")
	fun withdraw1() {
		// given
		val user = getUserFixture(Role.USER)

		doReturn(user).`when`(userReadFacade).findByIdWithAll(user.id)

		// when & then
		shouldNotThrow<Exception> { userService.withdraw(user) }

		verify(userReadFacade, times(1)).findByIdWithAll(user.id)
	}

	@TestWithDisplayName("유저가 없는 경우, 탈퇴에 실패한다.")
	fun withdraw2() {
		// given
		val user = getUserFixture(Role.USER)

		doThrow(CakkException(ReturnCode.NOT_EXIST_USER)).`when`(userReadFacade).findByIdWithAll(user.id)

		// when & then

		val exception = shouldThrow<CakkException> { userService.withdraw(user) }

		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_USER
		verify(userReadFacade, times(1)).findByIdWithAll(user.id)
		verify(userManagerFacade, never()).withdraw(user, supplyUserWithdrawalBy(user))
	}
}
