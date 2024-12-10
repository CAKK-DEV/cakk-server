package com.cakk.core.common.base

import java.time.LocalDate

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils

import com.cakk.common.enums.ProviderType
import com.cakk.common.enums.Role
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getEnumFixture
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getPointFixture
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@ExtendWith(MockitoExtension::class)
abstract class FacadeTest {

	protected fun getUserFixture(role: Role = Role.USER): UserEntity {
		return fixtureMonkey.giveMeBuilder(UserEntity::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("providerType", getEnumFixture(ProviderType::class.java))
			.set("providerId", getStringFixtureBw(1, 50))
			.set("email", getStringFixtureBw(1, 50))
			.set("nickname", getStringFixtureBw(1, 50))
			.set("birthday", LocalDate.now())
			.set("role", role)
			.sample()
	}

	protected fun getCakeFixture(): CakeEntity {
		return fixtureMonkey.giveMeBuilder(CakeEntity::class.java)
			.set("cakeImageUrl", getStringFixtureBw(1, 200))
			.set("cakeShop", getCakeShopFixture())
			.sample()
	}

	protected fun getCakeShopFixture(): CakeShopEntity {
		val cakeShop = fixtureMonkey.giveMeBuilder(CakeShopEntity::class.java)
			.set("shopName", getStringFixtureBw(10, 30))
			.set("thumbnailUrl", getStringFixtureBw(10, 200))
			.set("shopAddress", getStringFixtureBw(1, 50))
			.set("shopBio", getStringFixtureBw(1, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("location", getPointFixture())
			.sample()
		ReflectionTestUtils.setField(cakeShop, "heartCount", 0)
		ReflectionTestUtils.setField(cakeShop, "likeCount", 0)
		return cakeShop
	}
}
