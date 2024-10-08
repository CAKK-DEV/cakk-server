package com.cakk.core.dto.param.cake

import org.locationtech.jts.geom.Point

data class CakeSearchParam(
    val cakeId: Long?,
    val keyword: String?,
    val location: Point?,
    val pageSize: Int
)
