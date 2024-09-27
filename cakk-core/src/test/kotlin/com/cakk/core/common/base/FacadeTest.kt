package com.cakk.core.common.base

import java.time.LocalDate

import org.junit.jupiter.api.extension.ExtendWith
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.mockito.junit.jupiter.MockitoExtension

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin

import com.cakk.common.enums.Provider
import com.cakk.common.enums.Role
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getEnumFixture
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getPointFixture
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.User
import org.springframework.test.util.ReflectionTestUtils

private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER: Int = 4326

@ExtendWith(MockitoExtension::class)
abstract class FacadeTest {

	private val geometryFactory: GeometryFactory = GeometryFactory(PrecisionModel(), SPATIAL_REFERENCE_IDENTIFIER_NUMBER)

	protected fun getConstructorMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getUserFixture(role: Role = Role.USER): User {
		return fixtureMonkey.giveMeBuilder(User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("provider", getEnumFixture(Provider::class.java))
			.set("providerId", getStringFixtureBw(1, 50))
			.set("email", getStringFixtureBw(1, 50))
			.set("nickname", getStringFixtureBw(1, 50))
			.set("birthday", LocalDate.now())
			.set("role", role)
			.sample()
	}

	protected fun getCakeFixture(): Cake {
		return fixtureMonkey.giveMeBuilder(Cake::class.java)
			.set("cakeImageUrl", getStringFixtureBw(1, 200))
			.set("cakeShop", getCakeShopFixture())
			.sample()
	}

	protected fun getCakeShopFixture(): CakeShop {
		val cakeShop = fixtureMonkey.giveMeBuilder(CakeShop::class.java)
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
