package com.cakk.domain.entity.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import com.cakk.domain.annotation.TestWithDisplayName
import com.cakk.domain.common.base.DomainTest

class VerificationPolicyTest : DomainTest() {

	@TestWithDisplayName("인증 정책은 사용자를 케이크샵 주인으로 승격시켜준다")
	fun approveToBusinessOwner() {
		val verificationPolicy = verificationPolicy
		val verificationStatus = verificationPolicy.approveToBusinessOwner(VerificationStatus.PENDING)

		verificationStatus shouldBe VerificationStatus.APPROVED
	}

	@TestWithDisplayName("인증 정책은 사용자의 권한이 BUSINESS_OWNER가 아니고 인증 진행중이라면 케이크샵 주인 후보임을 알려준다")
	fun getIsCandidate() {
		val verificationPolicy = verificationPolicy

		verificationPolicy.isCandidate(VerificationStatus.PENDING) shouldBe true
	}

	@TestWithDisplayName("사장님 인증된 케이크 샵이 존재할 때, 인증 요청에 실패한다")
	fun registerCertificationInformation2() {
		//given
		val verificationPolicy = verificationPolicy
		val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.APPROVED)
		val user = getUserFixture(Role.USER)
		val param = getCertificationParamFixtureWithUser(user)

		//when
		val exception = shouldThrow<CakkException> {
			verificationPolicy.requestCertificationBusinessOwner(businessInformation, param)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.CAKE_SHOP_CERTIFICATED_ISSUE
	}

	@TestWithDisplayName("사장님 인증 요청이 진행중일 때, 인증 요청에 실패한다")
	fun registerCertificationInformation3() {
		//given
		val verificationPolicy = verificationPolicy
		val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING)
		val user = getUserFixture(Role.USER)
		val param = getCertificationParamFixtureWithUser(user)

		//when
		val exception = shouldThrow<CakkException> {
			verificationPolicy.requestCertificationBusinessOwner(businessInformation, param)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.CAKE_SHOP_CERTIFICATED_ISSUE
	}

	@TestWithDisplayName("사장님 인증 요청이 거절 됐을 때, 인증 요청에 실패한다")
	fun registerCertificationInformation4() {
		//given
		val verificationPolicy = verificationPolicy
		val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.REJECTED)
		val user = getUserFixture(Role.USER)
		val param = getCertificationParamFixtureWithUser(user)

		//when
		val exception = shouldThrow<CakkException> {
			verificationPolicy.requestCertificationBusinessOwner(businessInformation, param)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.CAKE_SHOP_CERTIFICATED_ISSUE
	}
}

