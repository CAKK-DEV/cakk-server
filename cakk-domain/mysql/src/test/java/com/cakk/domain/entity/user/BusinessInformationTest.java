package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.cakk.common.enums.Role;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.bo.DefaultVerificationPolicy;
import com.cakk.domain.mysql.bo.VerificationPolicy;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

import com.navercorp.fixturemonkey.customizer.Values;

class BusinessInformationTest extends DomainTest {


	private CakeShop getCakeShopFixture() {
		return getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(
				Arbitraries.doubles().between(-90, 90).sample(),
				Arbitraries.doubles().between(-180, 180).sample())
			)
			.sample();
	}

	private BusinessInformation getBusinessInformationFixture() {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.setNull("cakeShop")
			.setNull("user")
			.sample();
	}

	private BusinessInformation getBusinessInformationFixtureWithUser(VerificationStatus verificationStatus, Role role) {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.setNotNull("businessNumber")
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("cakeShop", getCakeShopFixture())
			.set("verificationStatus", verificationStatus)
			.set("user", Values.just(getUserFixture(role)))
			.sample();
	}

	private BusinessInformation getBusinessInformationFixtureWithCakeShop(VerificationStatus verificationStatus) {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("cakeShop", Values.just(getCakeShopFixture()))
			.set("verificationStatus", verificationStatus)
			.setNull("user")
			.sample();
	}

	private CertificationParam getCertificationParamFixtureWithUser(User user) {
		return getBuilderMonkey().giveMeBuilder(CertificationParam.class)
			.set("businessRegistrationImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("idCardImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(0))
			.set("emergencyContact", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("message", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("user", user)
			.sample();
	}

	@Test
	@DisplayName("케이크샵이 존재한다면 가게 정보와 함께 서비스에 인증요청을 한다")
	void getRequestCertificationMessage() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING);
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);
		String shopName = businessInformation.getCakeShop().getShopName();

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isEqualTo(shopName);
	}

	@Test
	@DisplayName("케이크샵이 존재하지 않는다면 가게 정보 없이 서비스에 인증요청을 한다")
	void getRequestCertificationMessage2() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixture();
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isNull();
	}

	@Test
	@DisplayName("사용자는 케이크샵의 주인으로 승격된다")
	void promotedByBusinessOwner() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING);
		VerificationPolicy verificationPolicy = new DefaultVerificationPolicy();
		User user = getUserFixture(Role.USER);

		//when
		businessInformation.updateBusinessOwner(verificationPolicy, user);

		//then
		assertThat(businessInformation.getUser()).isNotNull();
		assertThat(businessInformation.getUser().getRole()).isEqualTo(Role.BUSINESS_OWNER);
		assertThat(businessInformation.getVerificationStatus()).isEqualTo(VerificationStatus.APPROVED);
	}
}
