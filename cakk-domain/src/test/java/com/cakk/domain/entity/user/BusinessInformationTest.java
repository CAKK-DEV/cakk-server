package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

import net.jqwik.api.Arbitraries;

import com.cakk.common.enums.Role;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.event.shop.CertificationEvent;

class BusinessInformationTest extends DomainTest {

	private CakeShop getCakeShopFixture() {
		return getReflectionMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.sample();
	}

	private BusinessInformation getBusinessInformationFixture() {
		return getReflectionMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.setNull("cakeShop")
			.setNull("user")
			.sample();
	}

	private BusinessInformation getBusinessInformationFixtureWithCakeShop() {
		return getReflectionMonkey().giveMeBuilder(BusinessInformation.class)
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
			.set("user", user)
			.set("message", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.sample();
	}

	@RepeatedTest(100)
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

	@RepeatedTest(100)
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

	@RepeatedTest(100)
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
