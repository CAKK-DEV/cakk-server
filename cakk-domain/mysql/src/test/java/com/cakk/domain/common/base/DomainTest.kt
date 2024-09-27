package com.cakk.domain.common.base

import com.navercorp.fixturemonkey.customizer.Values

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.domain.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.domain.common.fixture.FixtureCommon.getPointFixture
import com.cakk.domain.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.domain.mysql.bo.user.DefaultVerificationPolicy
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.dto.param.user.CertificationParam
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User

abstract class DomainTest {

	protected fun getUserFixture(role: Role = Role.USER): User {
		return fixtureMonkey.giveMeBuilder(User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("email", getStringFixtureBw(1, 50))
			.set("role", role)
			.sample()
	}

	protected val verificationPolicy: VerificationPolicy
		get() = DefaultVerificationPolicy()

	protected val cakeShopFixture: CakeShop
		get() = fixtureMonkey.giveMeBuilder(CakeShop::class.java)
			.set("shopName", getStringFixtureBw(1, 30))
			.set("shopBio", getStringFixtureBw(1, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("likeCount", 0)
			.set("heartCount", 0)
			.set("location", getPointFixture())
			.sample()

	protected fun getBusinessInformationFixtureWithCakeShop(verificationStatus: VerificationStatus?): BusinessInformation {
		return fixtureMonkey.giveMeBuilder(BusinessInformation::class.java)
			.set("businessNumber", getStringFixtureBw(1, 20))
			.set("cakeShop", Values.just(cakeShopFixture))
			.set("verificationStatus", verificationStatus)
			.setNull("user")
			.sample()
	}

	protected fun getCertificationParamFixtureWithUser(user: User?): CertificationParam {
		return fixtureMonkey.giveMeBuilder(CertificationParam::class.java)
			.set("businessRegistrationImageUrl", getStringFixtureBw(1, 20))
			.set("idCardImageUrl", getStringFixtureBw(1, 20))
			.set("cakeShopId", getLongFixtureGoe(10))
			.set("emergencyContact", getStringFixtureBw(1, 20))
			.set("message", getStringFixtureBw(1, 20))
			.set("user", user)
			.sample()
	}

	protected val cakeFixture: Cake
		protected get() = fixtureMonkey.giveMeBuilder(Cake::class.java)
			.set("cakeImageUrl", getStringFixtureBw(1, 50))
			.set("cakeShop", Values.just(cakeShopFixture))
			.sample()
}

