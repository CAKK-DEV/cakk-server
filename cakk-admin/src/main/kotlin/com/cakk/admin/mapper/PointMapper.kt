package com.cakk.admin.mapper

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import java.util.*

private const val SPATIAL_REFERENCE_IDENTIFIER_NUMBER = 4326

private val geometryFactory = GeometryFactory(PrecisionModel(), SPATIAL_REFERENCE_IDENTIFIER_NUMBER)

fun supplyPointBy(latitude: Double?, longitude: Double?): Point? {
    if (Objects.isNull(latitude) || Objects.isNull(longitude)) {
        return null
    }
    return geometryFactory.createPoint(Coordinate(longitude!!, latitude!!))
}
