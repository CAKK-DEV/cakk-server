package com.cakk.core.mapper

import java.util.*

import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.cake.CakeImageWithShopInfoListResponse
import com.cakk.core.dto.response.like.HeartCakeImageListResponse
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam
import com.cakk.domain.mysql.dto.param.cake.CakeImageWithShopInfoResponseParam
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam
import com.cakk.domain.mysql.dto.param.tag.TagParam
import com.cakk.domain.mysql.entity.cake.Cake


fun supplyCakeImageWithShopInfoListResponse(
	cakeImages: List<CakeImageWithShopInfoResponseParam>
): CakeImageWithShopInfoListResponse {
	val size = cakeImages.size

	return CakeImageWithShopInfoListResponse(
		cakeImages,
		if(cakeImages.isEmpty()) null else cakeImages[size - 1].cakeId,
		cakeImages.size
	)
}

fun supplyCakeImageWithShopInfoListResponse(
	cakeImages: List<CakeImageWithShopInfoResponseParam>,
	cakeIds: List<Long>
): CakeImageWithShopInfoListResponse {
	val sortedCakeImages: MutableList<CakeImageWithShopInfoResponseParam> = ArrayList()

	for (cakeId in cakeIds) {
		cakeImages.stream()
			.filter { cakeImage: CakeImageWithShopInfoResponseParam -> cakeImage.cakeId == cakeId }
			.findFirst()
			.ifPresent { e: CakeImageWithShopInfoResponseParam -> sortedCakeImages.add(e) }
	}

	return CakeImageWithShopInfoListResponse(
		sortedCakeImages,
		null,
		cakeImages.size
	)
}

fun supplyHeartCakeImageListResponseBy(cakeImages: List<HeartCakeImageResponseParam>): HeartCakeImageListResponse {
	val size = cakeImages.size

	return HeartCakeImageListResponse(
		cakeImages,
		if (cakeImages.isEmpty()) null else cakeImages[size - 1].cakeHeartId,
		cakeImages.size
	)
}

fun cakeDetailResponseFromParam(param: CakeDetailParam): CakeDetailResponse {
	var tags = param.tags

	for (tagParam in tags) {
		if (isEmptyTag(tagParam)) {
			tags = HashSet()
			break
		}
	}

	return CakeDetailResponse(
		param.cakeImageUrl,
		param.cakeShopName,
		param.shopBio,
		param.cakeShopId,
		param.cakeCategories,
		tags
	)
}

fun supplyCakeBy(cakeImageUrl: String): Cake {
	return Cake.builder()
		.cakeImageUrl(cakeImageUrl)
		.build()
}

private fun isEmptyTag(tagParam: TagParam): Boolean {
	return Objects.isNull(tagParam.tagId) || Objects.isNull(tagParam.tagName)
}
