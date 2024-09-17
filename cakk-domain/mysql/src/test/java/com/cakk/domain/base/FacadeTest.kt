package com.cakk.domain.base

import com.cakk.common.enums.Provider
import com.cakk.common.enums.Role
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.User
import com.navercorp.fixturemonkey.FixtureMonkey
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

	private fun constructorMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build()
	}
    protected fun reflectionMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build()
	}

    protected fun builderMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build()
	}

    protected fun getUserFixture(role: Role?): User {
        return constructorMonkey().giveMeBuilder(User::class.java)
                .set("id", Arbitraries.longs().greaterOrEqual(10))
                .set("provider", Arbitraries.of(Provider::class.java))
                .set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
                .set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
                .set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
                .set("birthday", LocalDate.now())
                .set("role", role)
                .sample()
    }

    protected val cakeFixture: Cake
        protected get() = constructorMonkey().giveMeBuilder(Cake::class.java)
                .set("cakeImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
                .set("cakeShop", Values.just(cakeShopFixture))
                .sample()
    protected val cakeShopFixture: CakeShop
        protected get() = constructorMonkey().giveMeBuilder(CakeShop::class.java)
                .set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
                .set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
                .set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
                .set("likeCount", 0)
                .set("heartCount", 0)
                .set("location", supplyPointBy(
                        Arbitraries.doubles().between(-90.0, 90.0).sample(),
                        Arbitraries.doubles().between(-180.0, 180.0).sample())
                )
                .sample()

    companion object {
        private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326
        private val geometryFactory = GeometryFactory(
                PrecisionModel(),
                SPATIAL_REFERENCE_IDENTIFIER_NUMBER
        )

        fun supplyPointBy(latitude: Double?, longitude: Double?): Point {
            return geometryFactory.createPoint(Coordinate(longitude!!, latitude!!))
        }
    }
}

