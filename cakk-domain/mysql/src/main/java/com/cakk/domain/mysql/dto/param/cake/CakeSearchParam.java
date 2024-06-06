package com.cakk.domain.mysql.dto.param.cake;

import org.locationtech.jts.geom.Point;

public record CakeSearchParam(

	Long cursorId,
	String searchText,
	Point location,
	Integer pageSize
) {

}
