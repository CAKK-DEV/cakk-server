package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.cakk.common.enums.Role;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

class BusinessInformationTest extends DomainTest {

	private CakeShop getCakeShopFixture() {
		return getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(Arbitraries.doubles().sample(), Arbitraries.doubles().sample()))
			.sample();
	}

	private BusinessInformation getBusinessInformationFixture() {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.setNull("cakeShop")
			.setNull("user")
			.sample();
	}

	private BusinessInformation getBusinessInformationFixtureWithCakeShop() {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("cakeShop", getCakeShopFixture())
			.setNull("user")
			.sample();
	}

	private User getUserFixture() {
		return getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
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
	void 케이크샵이_존재한다면_가게_정보와_함께_서비스에_인증요청을_한다() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop();
		User user = getUserFixture();
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isNotNull();
	}

	@Test
	void 케이크샵이_존재하지_않는다면_가게_정보_없이_서비스에_인증요청을_한다() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixture();
		User user = getUserFixture();
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isNull();
	}

	@Test
	void 사용자는_케이크샵의_주인으로_승격된다() {
		//given
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop();
		User user = getUserFixture();

		//when
		businessInformation.promotedByBusinessOwner(user);

		//then
		assertThat(businessInformation.getUser()).isNotNull();
		assertThat(businessInformation.getCakeShop().getLinkedFlag()).isTrue();
		assertThat(businessInformation.getUser().getRole()).isEqualTo(Role.BUSINESS_OWNER);
	}
}
