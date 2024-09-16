package com.cakk.core.common.base

import com.cakk.common.enums.Gender
import com.cakk.common.enums.Provider
import com.cakk.common.enums.Role
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.User
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.instantiator.Instantiator
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector
import com.navercorp.fixturemonkey.customizer.Values
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin
import net.jqwik.api.Arbitraries
import org.junit.jupiter.api.extension.ExtendWith
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
abstract class FacadeTest {

	protected fun getConstructorMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getReflectionMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getBuilderMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getUserFixture(role: Role): User {
		return getConstructorMonkey().giveMeBuilder(User::class.java)
			.instantiate(
				User::class.java,
				Instantiator.constructor<Any>()
					.parameter(Long::class.javaPrimitiveType)
					.parameter(Provider::class.java)
					.parameter(String::class.java)
					.parameter(String::class.java)
					.parameter(String::class.java)
					.parameter(String::class.java)
					.parameter(Gender::class.java)
					.parameter(LocalDate::class.java)
					.parameter(String::class.java)
					.parameter(String::class.java)
					.parameter(role.javaClass)
			).sample()
	}

	protected fun getCakeFixture(): Cake {
		return getConstructorMonkey().giveMeBuilder(Cake::class.java)
			.set("cakeImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
			.set("cakeShop", Values.just(getCakeShopFixture()))
			.sample()
	}

	protected fun getCakeShopFixture(): CakeShop {
		return getConstructorMonkey().giveMeBuilder(CakeShop::class.java)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("likeCount", 0)
			.set("heartCount", 0)
			.set(
				"location", supplyPointBy(
					Arbitraries.doubles().between(-90.0, 90.0).sample(),
					Arbitraries.doubles().between(-180.0, 180.0).sample()
				)
			)
			.sample()
	}
}

private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER: Int = 4326

fun supplyPointBy(latitude: Double, longitude: Double): Point {
	return geometryFactory.createPoint(Coordinate(longitude, latitude))
}

private val geometryFactory: GeometryFactory = GeometryFactory(PrecisionModel(), SPATIAL_REFERENCE_IDENTIFIER_NUMBER)
