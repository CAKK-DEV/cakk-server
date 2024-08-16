package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.customizer.Values;

import com.cakk.common.enums.Role;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.bo.user.VerificationPolicy;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.event.shop.CertificationEvent;


class BusinessInformationTest extends DomainTest {

	private BusinessInformation getBusinessInformationFixtureWithUser(VerificationStatus verificationStatus, Role role) {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.setNotNull("businessNumber")
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("cakeShop", getCakeShopFixture())
			.set("verificationStatus", verificationStatus)
			.set("user", Values.just(getUserFixture(role)))
			.sample();
	}

	@Test
	@DisplayName("사장님 인증되지 않은 케이크 샵이 존재할 때, 가게 정보와 함께 서비스에 인증요청을 한다")
	void registerCertificationInformation1() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.UNREQUESTED);
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);
		String shopName = businessInformation.getCakeShop().getShopName();

		//when
		CertificationEvent certificationEvent = businessInformation.registerCertificationInformation(param);

		//then
		assertThat(businessInformation.getVerificationStatus()).isEqualTo(VerificationStatus.PENDING);
		assertThat(businessInformation.getBusinessRegistrationImageUrl()).isEqualTo(param.businessRegistrationImageUrl());
		assertThat(businessInformation.getIdCardImageUrl()).isEqualTo(param.idCardImageUrl());
		assertThat(businessInformation.getEmergencyContact()).isEqualTo(param.emergencyContact());
		assertThat(certificationEvent.shopName()).isEqualTo(shopName);
	}

	@Test
	@DisplayName("사용자는 케이크샵의 주인으로 승격된다")
	void promotedByBusinessOwner() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING);
		VerificationPolicy verificationPolicy = getVerificationPolicy();
		User user = getUserFixture(Role.USER);

		//when
		businessInformation.updateBusinessOwner(verificationPolicy, user);

		//then
		assertThat(businessInformation.getUser()).isNotNull();
		assertThat(businessInformation.getUser().getRole()).isEqualTo(Role.USER);
		assertThat(businessInformation.getVerificationStatus()).isEqualTo(VerificationStatus.APPROVED);
	}

	@Test
	@DisplayName("예비 사장님 여부 검사에서 인증 요청 상태라면, True를 반환한다")
	void isPendingVerificationTrue() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.PENDING, Role.USER);
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		//then
		assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isTrue();
	}

	@Test
	@DisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
	void isPendingVerificationFalse1() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER);
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		//then
		assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse();
	}

	@Test
	@DisplayName("예비 사장님 여부 검사에서 인증 완료 상태라면, False를 반환한다")
	void isPendingVerificationFalse2() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.APPROVED, Role.USER);
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		//then
		assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse();
	}

	@Test
	@DisplayName("예비 사장님 여부 검사에서 인증 요청 상태도 아니라면, False를 반환한다")
	void isPendingVerificationFalse3() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithUser(VerificationStatus.UNREQUESTED, Role.USER);
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		//then
		assertThat(businessInformation.isBusinessOwnerCandidate(verificationPolicy)).isFalse();
	}
}

