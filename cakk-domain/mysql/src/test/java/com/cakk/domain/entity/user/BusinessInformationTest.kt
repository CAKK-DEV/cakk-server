package com.cakk.domain.entity.user

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.base.DomainTest
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.navercorp.fixturemonkey.customizer.Values
import net.jqwik.api.Arbitraries
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class BusinessInformationTest : DomainTest() {
    private fun getBusinessInformationFixtureWithUser(verificationStatus: VerificationStatus, role: Role): BusinessInformation {
        return constructorMonkey.giveMeBuilder(BusinessInformation::class.java)
                .setNotNull("businessNumber")
                .set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
                .set("cakeShop", cakeShopFixture)
                .set("verificationStatus", verificationStatus)
                .set("user", Values.just(getUserFixture(role)))
                .sample()
    }

    @Test
    @DisplayName("사장님 인증되지 않은 케이크 샵이 존재할 때, 가게 정보와 함께 서비스에 인증요청을 한다")
    fun registerCertificationInformation1() {
        //given
        val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.UNREQUESTED)
        val user = getUserFixture(Role.USER)
        val param = getCertificationParamFixtureWithUser(user)
        val shopName = businessInformation.cakeShop.shopName

        //when
        val certificationEvent = businessInformation.registerCertificationInformation(param)

        //then
        Assertions.assertThat(businessInformation.verificationStatus).isEqualTo(VerificationStatus.PENDING)
        Assertions.assertThat(businessInformation.businessRegistrationImageUrl).isEqualTo(param.businessRegistrationImageUrl)
        Assertions.assertThat(businessInformation.idCardImageUrl).isEqualTo(param.idCardImageUrl)
        Assertions.assertThat(businessInformation.emergencyContact).isEqualTo(param.emergencyContact)
		Assertions.assertThat(certificationEvent.shopName).isEqualTo(shopName)
    }

    @Test
    @DisplayName("사용자는 케이크샵의 주인으로 승격된다")
    fun promotedByBusinessOwner() {
        //given
        val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING)
        val verificationPolicy = verificationPolicy
        val user = getUserFixture(Role.USER)

        //when
        businessInformation.updateBusinessOwner(verificationPolicy, user)

        //then
        Assertions.assertThat(businessInformation.user).isNotNull
        Assertions.assertThat(businessInformation.user.role).isEqualTo(Role.USER)
        Assertions.assertThat(businessInformation.verificationStatus).isEqualTo(VerificationStatus.APPROVED)
    }

    @DisplayName("예비 사장님 여부 검사에서 인증 요청 상태라면, True를 반환한다")
    @Test
    fun isPendingVerificationTrue() {
            //given
            val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.PENDING, Role.USER)
            val verificationPolicy = verificationPolicy

            //then
            Assertions.assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isTrue
        }

    @DisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
    @Test
    fun isPendingVerificationFalse1() {
            //given
            val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER)
            val verificationPolicy = verificationPolicy

            //then
            Assertions.assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse
        }

    @DisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
    @Test
    fun isPendingVerificationFalse2() {
            //given
            val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER)
            val verificationPolicy = verificationPolicy

            //then
            Assertions.assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse
        }

    @DisplayName("예비 사장님 여부 검사에서 인증 요청 상태도 아니라면, False를 반환한다")
    @Test
    fun isPendingVerificationFalse3() {
            //given
            val businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.UNREQUESTED, Role.USER)
            val verificationPolicy = verificationPolicy

            //then
            Assertions.assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse
        }
}
