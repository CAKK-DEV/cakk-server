package com.cakk.core.mapper

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel

private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326

private val geometryFactory = GeometryFactory(PrecisionModel(), SPATIAL_REFERENCE_IDENTIFIER_NUMBER)

fun supplyPointBy(latitude: Double?, longitude: Double?): Point? {
	if (latitude == null || longitude == null) {
		return null
	}

    return geometryFactory.createPoint(Coordinate(longitude, latitude))
}
