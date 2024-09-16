package com.cakk.domain.mysql.dto.param.cake;

import org.locationtech.jts.geom.Point;

public record CakeSearchParam(

	Long cakeId,
	String keyword,
	Point location,
	Integer pageSize
) {

}
