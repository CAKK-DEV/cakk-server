package com.cakk.domain.entity.user

import io.kotest.matchers.shouldBe

import com.navercorp.fixturemonkey.customizer.Values

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.annotation.TestWithDisplayName
import com.cakk.domain.common.base.DomainTest
import com.cakk.domain.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.domain.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.domain.mysql.entity.user.BusinessInformation

internal class BusinessInformationTest : DomainTest() {

	private fun getBusinessInformationFixtureWithUser(verificationStatus: VerificationStatus, role: Role): BusinessInformation {
		return fixtureMonkey.giveMeBuilder(BusinessInformation::class.java)
			.setNotNull("businessNumber")
			.set("businessNumber", getStringFixtureBw(1, 20))
			.set("cakeShop", cakeShopFixture)
			.set("verificationStatus", verificationStatus)
			.set("user", Values.just(getUserFixture()))
			.sample()
	}

	@TestWithDisplayName("사장님 인증되지 않은 케이크 샵이 존재할 때, 가게 정보와 함께 서비스에 인증요청을 한다")
	fun registerCertificationInformation1() {
		//given
		val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.UNREQUESTED)
		val user = getUserFixture(Role.USER)
		val param = getCertificationParamFixtureWithUser(user)
		val shopName = businessInformation.cakeShop.shopName

		//when
		val certificationEvent = businessInformation.registerCertificationInformation(param)

		//then
		businessInformation.verificationStatus shouldBe VerificationStatus.PENDING
		businessInformation.businessRegistrationImageUrl shouldBe param.businessRegistrationImageUrl
		businessInformation.idCardImageUrl shouldBe param.idCardImageUrl
		businessInformation.emergencyContact shouldBe param.emergencyContact
		certificationEvent.shopName shouldBe shopName
	}

	@TestWithDisplayName("사용자는 케이크샵의 주인으로 승격된다")
	fun promotedByBusinessOwner() {
		//given
		val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING)
		val verificationPolicy = verificationPolicy
		val user = getUserFixture()

		//when
		businessInformation.updateBusinessOwner(verificationPolicy, user)

		//then
		businessInformation.user shouldBe user
		businessInformation.user.role shouldBe Role.USER
		businessInformation.verificationStatus shouldBe VerificationStatus.APPROVED
	}

	@TestWithDisplayName("예비 사장님 여부 검사에서 인증 요청 상태라면, True를 반환한다")
	fun isPendingVerificationTrue() {
		//given
		val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.PENDING, Role.USER)
		val verificationPolicy = verificationPolicy

		//then
		businessInformation.isBusinessOwnerCandidate(verificationPolicy) shouldBe true
	}

	@TestWithDisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
	fun isPendingVerificationFalse1() {
		//given
		val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER)
		val verificationPolicy = verificationPolicy

		//then
		businessInformation.isBusinessOwnerCandidate(verificationPolicy) shouldBe false
	}

	@TestWithDisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
	fun isPendingVerificationFalse2() {
		//given
		val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER)
		val verificationPolicy = verificationPolicy

		//then
		businessInformation.isBusinessOwnerCandidate(verificationPolicy) shouldBe false
	}

	@TestWithDisplayName("예비 사장님 여부 검사에서 인증 요청 상태도 아니라면, False를 반환한다")
	fun isPendingVerificationFalse3() {
		//given
		val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.UNREQUESTED, Role.USER)
		val verificationPolicy = verificationPolicy

		//then
		businessInformation.isBusinessOwnerCandidate(verificationPolicy) shouldBe false
	}
}

