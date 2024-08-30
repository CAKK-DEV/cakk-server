package com.cakk.domain.base;

import java.time.LocalDate;

import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.junit.jupiter.MockitoExtension;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.customizer.Values;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import com.cakk.common.enums.Provider;
import com.cakk.common.enums.Role;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@ExtendWith(MockitoExtension.class)
public abstract class FacadeTest {

	private static final int SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326;

	private static final GeometryFactory geometryFactory = new GeometryFactory(
		new PrecisionModel(),
		SPATIAL_REFERENCE_IDENTIFIER_NUMBER
	);

	protected final FixtureMonkey getConstructorMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected User getUserFixture(final Role role) {
		return getConstructorMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("birthday", LocalDate.now())
			.set("role", role)
			.sample();
	}

	protected Cake getCakeFixture() {
		return getConstructorMonkey().giveMeBuilder(Cake.class)
			.set("cakeImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(50))
			.set("cakeShop", Values.just(getCakeShopFixture()))
			.sample();
	}

	protected CakeShop getCakeShopFixture() {
		return getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("likeCount", 0)
			.set("heartCount", 0)
			.set("location", supplyPointBy(
				Arbitraries.doubles().between(-90, 90).sample(),
				Arbitraries.doubles().between(-180, 180).sample())
			)
			.sample();
	}

	public static Point supplyPointBy(Double latitude, Double longitude) {
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}
}
