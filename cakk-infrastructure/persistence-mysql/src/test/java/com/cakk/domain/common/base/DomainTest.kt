package com.cakk.domain.common.base

import com.navercorp.fixturemonkey.customizer.Values

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.domain.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.domain.common.fixture.FixtureCommon.getPointFixture
import com.cakk.domain.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.infrastructure.persistence.bo.user.DefaultVerificationPolicy
import com.cakk.infrastructure.persistence.bo.user.VerificationPolicy
import com.cakk.infrastructure.persistence.param.user.CertificationParam
import com.cakk.infrastructure.persistence.entity.cake.Cake
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.user.BusinessInformation
import com.cakk.infrastructure.persistence.entity.user.User

abstract class DomainTest {

	protected fun getUserFixture(role: Role = Role.USER): com.cakk.infrastructure.persistence.entity.user.User {
		return fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("email", getStringFixtureBw(1, 50))
			.set("role", role)
			.sample()
	}

	protected val verificationPolicy: com.cakk.infrastructure.persistence.bo.user.VerificationPolicy
		get() = com.cakk.infrastructure.persistence.bo.user.DefaultVerificationPolicy()

	protected val cakeShopFixture: com.cakk.infrastructure.persistence.entity.shop.CakeShop
		get() = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.shop.CakeShop::class.java)
			.set("shopName", getStringFixtureBw(1, 30))
			.set("shopBio", getStringFixtureBw(1, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("likeCount", 0)
			.set("heartCount", 0)
			.set("location", getPointFixture())
			.sample()

	protected fun getBusinessInformationFixtureWithCakeShop(verificationStatus: VerificationStatus?): com.cakk.infrastructure.persistence.entity.user.BusinessInformation {
		return fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.BusinessInformation::class.java)
			.set("businessNumber", getStringFixtureBw(1, 20))
			.set("cakeShop", Values.just(cakeShopFixture))
			.set("verificationStatus", verificationStatus)
			.setNull("user")
			.sample()
	}

	protected fun getCertificationParamFixtureWithUser(user: com.cakk.infrastructure.persistence.entity.user.User?): com.cakk.infrastructure.persistence.param.user.CertificationParam {
		return fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.user.CertificationParam::class.java)
			.set("businessRegistrationImageUrl", getStringFixtureBw(1, 20))
			.set("idCardImageUrl", getStringFixtureBw(1, 20))
			.set("cakeShopId", getLongFixtureGoe(10))
			.set("emergencyContact", getStringFixtureBw(1, 20))
			.set("message", getStringFixtureBw(1, 20))
			.set("user", user)
			.sample()
	}

	protected val cakeFixture: com.cakk.infrastructure.persistence.entity.cake.Cake
		protected get() = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.cake.Cake::class.java)
			.set("cakeImageUrl", getStringFixtureBw(1, 50))
			.set("cakeShop", Values.just(cakeShopFixture))
			.sample()
}

