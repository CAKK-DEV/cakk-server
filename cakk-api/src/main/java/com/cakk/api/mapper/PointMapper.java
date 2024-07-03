package com.cakk.api.mapper;

import static java.util.Objects.*;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointMapper {

	private static final int SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326;

	private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SPATIAL_REFERENCE_IDENTIFIER_NUMBER);

	public static Point supplyPointBy(final Double latitude, final Double longitude) {
		if (isNull(latitude) || isNull(longitude)) {
			return null;
		}
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}
}

