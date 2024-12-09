package com.cakk.core.mapper

import java.util.*

import com.cakk.common.utils.keepOnlyNElements
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.ShopOperationParam
import com.cakk.core.dto.response.like.HeartCakeShopListResponse
import com.cakk.core.dto.response.shop.*
import com.cakk.infrastructure.persistence.bo.shop.CakeShopByLocationParam
import com.cakk.infrastructure.persistence.bo.shop.CakeShopBySearchParam
import com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation
import com.cakk.infrastructure.persistence.entity.user.BusinessInformation
import com.cakk.infrastructure.persistence.param.shop.*

fun supplyCakeShopBy(param: CreateShopParam): CakeShop {
	return CakeShop.builder()
		.shopName(param.shopName)
		.shopBio(param.shopBio)
		.shopDescription(param.shopDescription)
		.shopAddress(param.shopAddress)
		.location(supplyPointBy(param.latitude, param.longitude))
		.build()
}

fun supplyCakeShopOperationsBy(cakeShop: CakeShop, operationDays: List<ShopOperationParam>): List<CakeShopOperation> {
	return operationDays.map { supplyCakeShopOperationBy(it, cakeShop) }.toList()
}

fun supplyCakeShopOperationBy(operationDay: ShopOperationParam, cakeShop: CakeShop): CakeShopOperation {
	return CakeShopOperation.builder()
		.operationDay(operationDay.operationDay)
		.operationStartTime(operationDay.operationStartTime)
		.operationEndTime(operationDay.operationEndTime)
		.cakeShop(cakeShop)
		.build()
}

fun supplyCakeShopSimpleResponseBy(param: CakeShopSimpleParam): CakeShopSimpleResponse {
	return CakeShopSimpleResponse(
		cakeShopId = param.cakeShopId,
		thumbnailUrl = param.thumbnailUrl,
		cakeShopName = param.cakeShopName,
		cakeShopBio = param.cakeShopBio
	)
}

fun supplyCakeShopDetailResponseBy(param: CakeShopDetailParam): CakeShopDetailResponse {
	return CakeShopDetailResponse(
		cakeShopId = param.cakeShopId,
		cakeShopName = param.shopName,
		thumbnailUrl = param.thumbnailUrl,
		cakeShopBio = param.shopBio,
		cakeShopDescription = param.shopDescription,
		operationDays = param.operationDays,
		links = param.links
	)
}

fun supplyCakeShopInfoResponseBy(param: CakeShopInfoParam): CakeShopInfoResponse {
	return CakeShopInfoResponse(
		shopAddress = param.shopAddress,
		longitude = param.point.x,
		latitude = param.point.y,
		shopOperationDays = param.shopOperationDays
	)
}

fun supplyCakeShopByMapResponseBy(params: List<CakeShopByLocationParam>): CakeShopByMapResponse {
	return CakeShopByMapResponse(params.map { supplyCakeShopLocationResponseParamBy(it) }.toList())
}

fun supplyCakeShopSearchResponseBy(cakeShops: List<CakeShopBySearchParam>): CakeShopSearchResponse {
	val size = cakeShops.size
	val cakeShopSearchResponseParams = cakeShops.map { supplyCakeShopSearchResponseParamListBy(it) }.toList()

	return CakeShopSearchResponse(
		cakeShops = cakeShopSearchResponseParams,
		lastCakeShopId = if (cakeShops.isEmpty()) null else cakeShops[size - 1].cakeShopId,
		size = cakeShops.size
	)
}

fun supplyHeartCakeShopListResponseBy(cakeShops: List<HeartCakeShopResponseParam>): HeartCakeShopListResponse {
	val size = cakeShops.size
	cakeShops.forEach { keepOnlyNElements(it.cakeImageUrls, 4) }

	return HeartCakeShopListResponse(
		cakeShops = cakeShops,
		lastCakeShopHeartId = if (cakeShops.isEmpty()) null else cakeShops[size - 1].cakeShopHeartId,
		size = size
	)
}

fun supplyCakeShopBySearchParamListBy(cakeShops: List<CakeShop>): List<CakeShopBySearchParam> {
	return cakeShops.map { supplyCakeShopBySearchParamBy(it) }.toList()
}

fun supplyCakeShopOwnerResponseBy(isOwned: Boolean): CakeShopOwnerResponse {
	return CakeShopOwnerResponse(isOwned)
}

fun supplyCakeShopCreateResponseBy(cakeShop: CakeShop): CakeShopCreateResponse {
	return CakeShopCreateResponse(cakeShop.id)
}

fun supplyCakeShopByMineResponseBy(result: List<BusinessInformation>): CakeShopByMineResponse {
	if (result.isEmpty()) {
		return CakeShopByMineResponse(false, null)
	}
	return CakeShopByMineResponse(true, result[0].cakeShop.id)
}

private fun supplyCakeShopLocationResponseParamBy(param: CakeShopByLocationParam): CakeShopLocationResponseParam {
	return CakeShopLocationResponseParam(
		param.cakeShopId,
		param.thumbnailUrl,
		param.cakeShopName,
		param.cakeShopBio,
		param.cakeImageUrls,
		param.longitude,
		param.latitude
	)
}

private fun supplyCakeShopSearchResponseParamListBy(param: CakeShopBySearchParam): CakeShopSearchResponseParam {
	return CakeShopSearchResponseParam(
		param.cakeShopId,
		param.thumbnailUrl,
		param.cakeShopName,
		param.cakeShopBio,
		param.cakeImageUrls,
		param.operationDays
	)
}

private fun supplyCakeShopBySearchParamBy(cakeShop: CakeShop): CakeShopBySearchParam {
	return CakeShopBySearchParam.builder()
		.cakeShopId(cakeShop.id)
		.thumbnailUrl(cakeShop.thumbnailUrl)
		.cakeShopName(cakeShop.shopName)
		.cakeShopBio(cakeShop.shopBio)
		.cakeImageUrls(cakeShop.cakes.map { it.cakeImageUrl }.toSet())
		.operationDays(cakeShop.cakeShopOperations.map { supplyCakeShopOperationParamBy(it) }.toSet())
		.build()
}

private fun supplyCakeShopOperationParamBy(cakeShopOperation: CakeShopOperation): CakeShopOperationParam {
	return CakeShopOperationParam(
		cakeShopOperation.operationDay,
		cakeShopOperation.operationStartTime,
		cakeShopOperation.operationEndTime
	)
}

private fun addLink(cakeShopLinks: MutableList<CakeShopLink>, cakeShopLink: CakeShopLink) {
	if (Objects.nonNull(cakeShopLink)) {
		cakeShopLinks.add(cakeShopLink)
	}
}
