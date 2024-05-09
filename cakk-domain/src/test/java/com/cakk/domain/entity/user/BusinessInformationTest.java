package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import net.jqwik.api.Arbitraries;

import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.event.shop.CertificationEvent;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

public class BusinessInformationTest {

	@Test
	public void 케이크샵이_존재한다면_가게_정보와_함께_서비스에_인증요청을_한다() {
		//given
		CakeShop cakeShop = getReflectionMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.sample();
		BusinessInformation businessInformation = getReflectionMonkey().giveMeBuilder(BusinessInformation.class)
			.set("cakeShop", cakeShop)
			.setNull("user").sample();
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.sample();
		CertificationParam param = getBuilderMonkey().giveMeBuilder(CertificationParam.class)
			.set("user", user)
			.sample();

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isNotNull();
	}

	@Test
	public void 케이크샵이_존재하지_않는다면_가게_정보_없이_서비스에_인증요청을_한다() {
		//given
		BusinessInformation businessInformation = getReflectionMonkey().giveMeBuilder(BusinessInformation.class).sample();
		User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.sample();
		CertificationParam param = getBuilderMonkey().giveMeBuilder(CertificationParam.class)
			.set("user", user)
			.sample();

		//when
		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);

		//then
		assertThat(certificationEvent.shopName()).isNull();
	}

	@Test
	public void 사용자는_케이크샵의_주인으로_승격된다() {
		//given
		CakeShop cakeShop = getReflectionMonkey().giveMeOne(CakeShop.class);
		BusinessInformation businessInformation = getReflectionMonkey().giveMeBuilder(BusinessInformation.class)
			.set("cakeShop", cakeShop)
			.sample();
		User user = getReflectionMonkey().giveMeOne(User.class);

		//when
		businessInformation.promotedByBusinessOwner(user);

		//then
		assertThat(businessInformation.getUser()).isNotNull();
		assertThat(businessInformation.getCakeShop().getLinkedFlag()).isTrue();
	}

	public FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	public FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}
}
