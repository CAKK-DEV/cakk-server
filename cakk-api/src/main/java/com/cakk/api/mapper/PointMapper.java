package com.cakk.api.mapper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointMapper {

	private static final int SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326;

	private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),
		SPATIAL_REFERENCE_IDENTIFIER_NUMBER);

	public static Point supplyPointBy(Double latitude, Double longitude) {
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}
}

