package com.cakk.api.common.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import com.cakk.common.enums.Provider;
import com.cakk.domain.mysql.config.JpaConfig;
import com.cakk.domain.mysql.entity.user.User;

@Import(JpaConfig.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

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

	protected User getUser() {
		return getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();
	}

	public static Point supplyPointBy(Double latitude, Double longitude) {
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}
}
