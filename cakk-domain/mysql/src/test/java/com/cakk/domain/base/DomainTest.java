package com.cakk.domain.base;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public abstract class DomainTest {

	private static final int SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326;

	private static final GeometryFactory geometryFactory = new GeometryFactory(
		new PrecisionModel(),
		SPATIAL_REFERENCE_IDENTIFIER_NUMBER
	);

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getConstructorMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}

	public static Point supplyPointBy(Double latitude, Double longitude) {
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}
}
