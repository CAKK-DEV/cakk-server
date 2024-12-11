package com.cakk.domain.common.base

import com.navercorp.fixturemonkey.customizer.Values

import com.cakk.common.enums.Role
import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.domain.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.domain.common.fixture.FixtureCommon.getPointFixture
import com.cakk.domain.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

abstract class DomainTest {

	protected fun getUserFixture(role: Role = Role.USER): UserEntity {
		return fixtureMonkey.giveMeBuilder(UserEntity::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("email", getStringFixtureBw(1, 50))
			.set("role", role)
			.sample()
	}

	protected val verificationPolicy: com.cakk.infrastructure.persistence.bo.user.VerificationPolicy
		get() = com.cakk.infrastructure.persistence.bo.user.DefaultVerificationPolicy()

	protected val cakeShopFixture: CakeShopEntity
		get() = fixtureMonkey.giveMeBuilder(CakeShopEntity::class.java)
			.set("shopName", getStringFixtureBw(1, 30))
			.set("shopBio", getStringFixtureBw(1, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("likeCount", 0)
			.set("heartCount", 0)
			.set("location", getPointFixture())
			.sample()

	protected fun getBusinessInformationFixtureWithCakeShop(verificationStatus: VerificationStatus?): BusinessInformationEntity {
		return fixtureMonkey.giveMeBuilder(BusinessInformationEntity::class.java)
			.set("businessNumber", getStringFixtureBw(1, 20))
			.set("cakeShop", Values.just(cakeShopFixture))
			.set("verificationStatus", verificationStatus)
			.setNull("user")
			.sample()
	}

	protected fun getCertificationParamFixtureWithUser(userEntity: UserEntity?): com.cakk.infrastructure.persistence.param.user.CertificationParam {
		return fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.user.CertificationParam::class.java)
			.set("businessRegistrationImageUrl", getStringFixtureBw(1, 20))
			.set("idCardImageUrl", getStringFixtureBw(1, 20))
			.set("cakeShopId", getLongFixtureGoe(10))
			.set("emergencyContact", getStringFixtureBw(1, 20))
			.set("message", getStringFixtureBw(1, 20))
			.set("user", userEntity)
			.sample()
	}

	protected val cakeFixture: CakeEntity
		protected get() = fixtureMonkey.giveMeBuilder(CakeEntity::class.java)
			.set("cakeImageUrl", getStringFixtureBw(1, 50))
			.set("cakeShop", Values.just(cakeShopFixture))
			.sample()
}

