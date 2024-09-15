package com.cakk.domain.entity.user

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import com.cakk.domain.base.DomainTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class VerificationPolicyTest : DomainTest() {
    @Test
    @DisplayName("인증 정책은 사용자를 케이크샵 주인으로 승격시켜준다")
    fun approveToBusinessOwner() {
        val verificationPolicy = verificationPolicy
        val verificationStatus = verificationPolicy.approveToBusinessOwner(VerificationStatus.PENDING)
        Assertions.assertThat(verificationStatus).isEqualTo(VerificationStatus.APPROVED)
    }

    @get:DisplayName("인증 정책은 사용자의 권한이 BUSINESS_OWNER가 아니고 인증 진행중이라면 케이크샵 주인 후보임을 알려준다")
    @get:Test
    val isCandidate: Unit
        get() {
            val verificationPolicy = verificationPolicy
            Assertions.assertThat(verificationPolicy.isCandidate(VerificationStatus.PENDING)).isTrue
        }

    @Test
    @DisplayName("사장님 인증된 케이크 샵이 존재할 때, 인증 요청에 실패한다")
    fun registerCertificationInformation2() {
        //given
        val verificationPolicy = verificationPolicy
        val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.APPROVED)
        val user = getUserFixture(Role.USER)
        val param = getCertificationParamFixtureWithUser(user)

        //when, then
        org.junit.jupiter.api.Assertions.assertThrowsExactly(CakkException::class.java
        ) { verificationPolicy.requestCertificationBusinessOwner(businessInformation, param) }
    }

    @Test
    @DisplayName("사장님 인증 요청이 진행중일 때, 인증 요청에 실패한다")
    fun registerCertificationInformation3() {
        //given
        val verificationPolicy = verificationPolicy
        val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING)
        val user = getUserFixture(Role.USER)
        val param = getCertificationParamFixtureWithUser(user)

        //when, then
        org.junit.jupiter.api.Assertions.assertThrowsExactly(CakkException::class.java
        ) { verificationPolicy.requestCertificationBusinessOwner(businessInformation, param) }
    }

    @Test
    @DisplayName("사장님 인증 요청이 거절 됐을 때, 인증 요청에 실패한다")
    fun registerCertificationInformation4() {
        //given
        val verificationPolicy = verificationPolicy
        val businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.REJECTED)
        val user = getUserFixture(Role.USER)
        val param = getCertificationParamFixtureWithUser(user)

        //when, then
        org.junit.jupiter.api.Assertions.assertThrowsExactly(CakkException::class.java
        ) { verificationPolicy.requestCertificationBusinessOwner(businessInformation, param) }
    }
}
