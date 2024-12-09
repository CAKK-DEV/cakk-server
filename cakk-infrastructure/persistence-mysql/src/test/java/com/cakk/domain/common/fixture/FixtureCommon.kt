package com.cakk.domain.common.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin
import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.arbitraries.DoubleArbitrary
import net.jqwik.api.arbitraries.LongArbitrary
import net.jqwik.api.arbitraries.StringArbitrary
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import java.time.LocalDate
import java.time.LocalDateTime

private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER: Int = 4326

object FixtureCommon {

	val fixtureMonkey: FixtureMonkey
		get() {
			return FixtureMonkey.builder()
				.plugin(JakartaValidationPlugin())
				.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
				.defaultNotNull(true)
				.build()
		}

	fun getPointFixture(): Point {
		return geometryFactory.createPoint(
            Coordinate(
                getDoubleFixtureBw(-180.0, 180.0).sample(),
                getDoubleFixtureBw(-90.0, 90.0).sample()
            )
		)
	}

	fun getDoubleFixtureBw(min: Double, max: Double): DoubleArbitrary {
		return Arbitraries.doubles().between(min, max);
	}

	fun getLongFixtureGoe(value: Long): LongArbitrary {
		return Arbitraries.longs().greaterOrEqual(value)
	}

	fun getLongFixtureBw(min: Long, max: Long): LongArbitrary {
		return Arbitraries.longs().between(min, max)
	}

	fun getStringFixtureEq(value: Int): StringArbitrary {
		return Arbitraries.strings().alpha().ofLength(value)
	}

	fun getStringFixtureBw(min: Int, max: Int): StringArbitrary {
		return Arbitraries.strings().alpha().ofMinLength(min).ofMaxLength(max)
	}

	fun <T : Enum<T>> getEnumFixture(enumClass: Class<T>): Arbitrary<T> {
		return Arbitraries.of(enumClass)
	}

	fun getDateFixture(): LocalDate {
		return fixtureMonkey.giveMeBuilder(LocalDate::class.java).sample();
	}

	fun getDateTimeFixture(): LocalDateTime {
		return fixtureMonkey.giveMeBuilder(LocalDateTime::class.java).sample();
	}

	private val geometryFactory: GeometryFactory = GeometryFactory(
        PrecisionModel(),
        SPATIAL_REFERENCE_IDENTIFIER_NUMBER
    )
}
