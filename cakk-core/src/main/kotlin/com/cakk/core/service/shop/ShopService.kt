package com.cakk.core.service.shop

import java.util.*

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.dto.param.search.CakeShopSearchByViewsParam
import com.cakk.core.dto.param.search.SearchShopByLocationParam
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.dto.response.shop.*
import com.cakk.core.facade.cake.BusinessInformationReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.shop.CakeShopManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.*
import com.cakk.domain.mysql.bo.shop.CakeShops
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam
import com.cakk.domain.mysql.dto.param.user.CertificationParam
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository

@Service
class ShopService(
	private val userReadFacade: UserReadFacade,
	private val cakeShopReadFacade: CakeShopReadFacade,
	private val businessInformationReadFacade: BusinessInformationReadFacade,
	private val cakeShopManageFacade: CakeShopManageFacade,
	private val cakeShopViewsRedisRepository: CakeShopViewsRedisRepository,
	private val verificationPolicy: VerificationPolicy,
	private val eventPublisher: ApplicationEventPublisher
) {


	@Transactional
	fun createCakeShopByCertification(param: CreateShopParam): CakeShopCreateResponse {
		val cakeShop = supplyCakeShopBy(param)
		val businessInformation = supplyBusinessInformationBy(param, cakeShop)
		val cakeShopOperations = supplyCakeShopOperationsBy(cakeShop, param.operationDays)
		val cakeShopLinks = supplyCakeShopLinksBy(cakeShop, param.links)

		val result = cakeShopManageFacade.create(
			cakeShop = cakeShop,
			cakeShopOperations = cakeShopOperations,
			businessInformation = businessInformation,
			cakeShopLinks = cakeShopLinks
		)

		return supplyCakeShopCreateResponseBy(result)
	}

	@Transactional
	fun promoteUserToBusinessOwner(param: PromotionParam) {
		val user = userReadFacade.findByUserId(param.userId)
		val businessInformation = cakeShopReadFacade.findBusinessInformationWithShop(param.cakeShopId)

		businessInformation.updateBusinessOwner(verificationPolicy, user)
	}

	@Transactional
	fun updateBasicInformation(param: CakeShopUpdateParam) {
		val cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.cakeShopId, param.user)

		cakeShop.updateBasicInformation(param)
	}

	@Transactional
	fun updateShopLinks(param: UpdateLinkParam) {
		val cakeShop = cakeShopReadFacade.searchWithShopLinks(param.user, param.cakeShopId)
		cakeShop.updateShopLinks(param.cakeShopLinks)
	}

	@Transactional
	fun updateShopAddress(param: UpdateShopAddressParam) {
		val cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.cakeShopId, param.user)
		cakeShop.updateShopAddress(param)
	}

	@Transactional
	fun updateShopOperationDays(param: UpdateShopOperationParam) {
		val cakeShop = cakeShopReadFacade.searchWithOperations(param.user, param.cakeShopId)
		cakeShop.updateShopOperationDays(param.cakeShopOperations)
	}

	@Transactional(readOnly = true)
	fun getMyBusinessId(user: User): CakeShopByMineResponse {
		val result = businessInformationReadFacade.findAllWithCakeShopByUser(user)
		return supplyCakeShopByMineResponseBy(result)
	}

	@Transactional
	fun requestCertificationBusinessOwner(param: CertificationParam) {
		val businessInformation = cakeShopReadFacade.findBusinessInformationByCakeShopId(param.cakeShopId)
		val certificationEvent = verificationPolicy.requestCertificationBusinessOwner(businessInformation, param)

		eventPublisher.publishEvent(certificationEvent)
	}

	@Transactional(readOnly = true)
	fun searchSimpleById(cakeShopId: Long): CakeShopSimpleResponse {
		val cakeShop = cakeShopReadFacade.searchSimpleById(cakeShopId)

		return supplyCakeShopSimpleResponseBy(cakeShop)
	}

	@Transactional(readOnly = true)
	fun searchDetailById(cakeShopId: Long): CakeShopDetailResponse {
		val cakeShop = cakeShopReadFacade.searchDetailById(cakeShopId)
		val event = supplyCakeShopIncreaseViewsEventBy(cakeShopId)

		eventPublisher.publishEvent(event)

		return supplyCakeShopDetailResponseBy(cakeShop)
	}

	@Transactional(readOnly = true)
	fun searchInfoById(cakeShopId: Long): CakeShopInfoResponse {
		val cakeShopInfo = cakeShopReadFacade.searchInfoById(cakeShopId)

		return supplyCakeShopInfoResponseBy(cakeShopInfo)
	}

	@Transactional(readOnly = true)
	fun searchShop(param: SearchShopByLocationParam): CakeShopByMapResponse {
		val longitude = param.longitude
		val latitude = param.latitude
		val distance = param.distance
		val point = supplyPointBy(latitude, longitude)

		val result = cakeShopReadFacade.searchShopByLocationBased(point, distance)
		val cakeShops = CakeShops(result, 4)

		return supplyCakeShopByMapResponseBy(cakeShops.cakeShops)
	}

	@Transactional(readOnly = true)
	fun searchShopByKeyword(param: CakeShopSearchParam): CakeShopSearchResponse {
		val pageSize = param.pageSize
		val result = cakeShopReadFacade.searchShopBySearch(param)
		val cakeShopBySearchParams = supplyCakeShopBySearchParamListBy(result)

		val cakeShops = CakeShops(cakeShopBySearchParams, 4, pageSize)

		param.keyword?.let {
			val event = supplyIncreaseSearchCountEventBy(param.keyword)
			eventPublisher.publishEvent(event)
		}

		return supplyCakeShopSearchResponseBy(cakeShops.cakeShops)
	}

	@Transactional(readOnly = true)
	fun searchCakeShopsByCursorAndViews(param: CakeShopSearchByViewsParam): CakeShopSearchResponse {
		val offset = param.offset ?: 0
		val pageSize = param.pageSize
		val cakeShopIds = cakeShopViewsRedisRepository.findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())

		if (Objects.isNull(cakeShopIds) || cakeShopIds.isEmpty()) {
			return supplyCakeShopSearchResponseBy(listOf())
		}

		val result = cakeShopReadFacade.searchShopsByShopIds(cakeShopIds)
		val cakeShopBySearchParams = supplyCakeShopBySearchParamListBy(result)
		val cakeShops = CakeShops(cakeShopBySearchParams, 6, pageSize)

		return supplyCakeShopSearchResponseBy(cakeShops.cakeShops)
	}

	@Transactional(readOnly = true)
	fun isExistBusinessInformation(owner: User, cakeShopId: Long): CakeShopOwnerResponse {
		val isOwned = businessInformationReadFacade.isExistBusinessInformation(owner, cakeShopId)

		return supplyCakeShopOwnerResponseBy(isOwned)
	}

	@Transactional(readOnly = true)
	fun getBusinessOwnerCandidates(): CakeShopOwnerCandidatesResponse {
			val businessInformation = businessInformationReadFacade.findAllCakeShopBusinessOwnerCandidates().filter {
				it.isBusinessOwnerCandidate(verificationPolicy)
			}.toList()

			return supplyCakeShopOwnerCandidatesResponseBy(businessInformation)
		}

	@Transactional(readOnly = true)
	fun getCandidateInformation(userId: Long): CakeShopOwnerCandidateResponse {
		val businessInformation = businessInformationReadFacade.findByUserId(userId)

		return supplyCakeShopOwnerCandidateResponseBy(businessInformation)
	}
}
