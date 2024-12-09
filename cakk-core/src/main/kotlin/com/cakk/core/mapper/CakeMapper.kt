package com.cakk.core.mapper

import java.util.*

import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.cake.CakeImageWithShopInfoListResponse
import com.cakk.core.dto.response.like.HeartCakeImageListResponse
import com.cakk.infrastructure.persistence.param.cake.CakeDetailParam
import com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam
import com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam
import com.cakk.infrastructure.persistence.param.tag.TagParam
import com.cakk.infrastructure.persistence.entity.cake.Cake


fun supplyCakeImageWithShopInfoListResponse(
	cakeImages: List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam>
): CakeImageWithShopInfoListResponse {
	val size = cakeImages.size

	return CakeImageWithShopInfoListResponse(
		cakeImages,
		if(cakeImages.isEmpty()) null else cakeImages[size - 1].cakeId,
		cakeImages.size
	)
}

fun supplyCakeImageWithShopInfoListResponse(
	cakeImages: List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam>,
	cakeIds: List<Long>
): CakeImageWithShopInfoListResponse {
	val sortedCakeImages: MutableList<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam> = ArrayList()

	for (cakeId in cakeIds) {
		cakeImages.stream()
			.filter { cakeImage: com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam -> cakeImage.cakeId == cakeId }
			.findFirst()
			.ifPresent { e: com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam -> sortedCakeImages.add(e) }
	}

	return CakeImageWithShopInfoListResponse(
		sortedCakeImages,
		null,
		cakeImages.size
	)
}

fun supplyHeartCakeImageListResponseBy(cakeImages: List<com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam>): HeartCakeImageListResponse {
	val size = cakeImages.size

	return HeartCakeImageListResponse(
		cakeImages,
		if (cakeImages.isEmpty()) null else cakeImages[size - 1].cakeHeartId,
		cakeImages.size
	)
}

fun cakeDetailResponseFromParam(param: com.cakk.infrastructure.persistence.param.cake.CakeDetailParam): CakeDetailResponse {
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

fun supplyCakeBy(cakeImageUrl: String): com.cakk.infrastructure.persistence.entity.cake.Cake {
	return com.cakk.infrastructure.persistence.entity.cake.Cake.builder()
		.cakeImageUrl(cakeImageUrl)
		.build()
}

private fun isEmptyTag(tagParam: com.cakk.infrastructure.persistence.param.tag.TagParam): Boolean {
	return Objects.isNull(tagParam.tagId) || Objects.isNull(tagParam.tagName)
}
