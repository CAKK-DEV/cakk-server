package com.cakk.core.facade

import org.mockito.Mockito.*

import java.time.LocalDateTime
import java.util.Optional

import org.mockito.InjectMocks
import org.mockito.Mock

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import net.jqwik.api.Arbitraries

import com.cakk.common.enums.Gender
import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.Role
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.FacadeTest
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam
import com.cakk.domain.mysql.entity.user.UserWithdrawal
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository
import com.cakk.domain.mysql.repository.jpa.UserWithdrawalJpaRepository

internal class UserManageFacadeTest : FacadeTest() {

	@InjectMocks
	private lateinit var userManageFacade: UserManageFacade

	@Mock
    private lateinit var userJpaRepository: UserJpaRepository

	@Mock
	private lateinit var userWithdrawalJpaRepository: UserWithdrawalJpaRepository

    @TestWithDisplayName("유저를 생성한다")
    fun create() {
        // given
        val user = getUserFixture(Role.USER)

		doReturn(Optional.ofNullable(null)).`when`(userJpaRepository).findByProviderId(user.providerId)
		doReturn(user).`when`(userJpaRepository).save(user)

        // when
		userManageFacade.create(user)

        // then
		verify(userJpaRepository, times(1)).findByProviderId(user.providerId)
		verify(userJpaRepository, times(1)).save(user)
    }

    @TestWithDisplayName("유저가 이미 있으면 User 생성에 실패한다")
    fun create2() {
        // given
        val user = getUserFixture(Role.USER)

		doReturn(Optional.of(user)).`when`(userJpaRepository).findByProviderId(user.providerId)

        // when
		val exception = shouldThrow<CakkException> {
			userManageFacade.create(user)
		}

        // then
		exception.getReturnCode() shouldBe ReturnCode.ALREADY_EXIST_USER
		verify(userJpaRepository, times(1)).findByProviderId(user.providerId)
		verify(userJpaRepository, never()).save(user)
    }

    @TestWithDisplayName("유저 정보를 수정한다")
    fun updateProfile() {
        // given
        val user = getUserFixture(Role.USER)
        val param = getConstructorMonkey().giveMeBuilder(ProfileUpdateParam::class.java)
            .set("profileImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(100))
            .set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
            .set("email", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
            .set("gender", Arbitraries.of(Gender::class.java))
            .set("birthday", java.time.LocalDate.now())
            .sample()

        // when
		userManageFacade.updateProfile(user, param)

        // then
		user.profileImageUrl shouldBe param.profileImageUrl
		user.nickname shouldBe param.nickname
		user.email shouldBe param.email
		user.gender shouldBe param.gender
		user.birthday shouldBe param.birthday
    }

    @TestWithDisplayName("유저를 탈퇴한다")
    fun withdraw() {
        // given
        val user = getUserFixture(Role.USER)
        val withdrawal: UserWithdrawal = getConstructorMonkey().giveMeBuilder(UserWithdrawal::class.java)
            .set("email", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
            .set("gender", Arbitraries.of(Gender::class.java))
            .set("birthday", java.time.LocalDate.now())
            .set("role", Arbitraries.of(Role::class.java))
            .set("withdrawalDate", LocalDateTime.now())
            .sample()

        // when
		userManageFacade.withdraw(user, withdrawal)

        // then
		verify(userWithdrawalJpaRepository, times(1)).save(withdrawal)
		verify(userJpaRepository, times(1)).delete(user)
    }
}
