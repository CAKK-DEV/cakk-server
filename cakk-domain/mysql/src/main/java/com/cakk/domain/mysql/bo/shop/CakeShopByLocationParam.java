package com.cakk.domain.mysql.bo.shop;

import java.util.Set;

import org.locationtech.jts.geom.Point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class CakeShopByLocationParam extends CakeShopParam {

	private Double longitude;
	private Double latitude;

	public CakeShopByLocationParam(Long cakeShopId, String thumbnailUrl, String cakeShopName, String cakeShopBio,
		Set<String> cakeImageUrls, Point location) {
		super(cakeShopId, thumbnailUrl, cakeShopName, cakeShopBio, cakeImageUrls);
		longitude = location.getX();
		latitude = location.getY();
	}
}
