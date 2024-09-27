package com.cakk.core.service.cake

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.dto.event.IncreaseSearchCountEvent
import com.cakk.core.dto.param.cake.*
import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.cake.CakeImageListResponse
import com.cakk.core.facade.cake.CakeManageFacade
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.tag.TagReadFacade
import com.cakk.core.mapper.cakeDetailResponseFromParam
import com.cakk.core.mapper.supplyCakeImageListResponse
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.redis.repository.CakeViewsRedisRepository

@Transactional(readOnly = true)
@Service
class CakeService(
	private val cakeReadFacade: CakeReadFacade,
	private val tagReadFacade: TagReadFacade,
	private val cakeShopReadFacade: CakeShopReadFacade,
	private val cakeManageFacade: CakeManageFacade,
	private val cakeViewsRedisRepository: CakeViewsRedisRepository,
	private val eventPublisher: ApplicationEventPublisher
) {


    fun findCakeImagesByCursorAndCategory(dto: CakeSearchByCategoryParam): CakeImageListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)

        return supplyCakeImageListResponse(cakeImages)
    }

    fun findCakeImagesByCursorAndCakeShopId(dto: CakeSearchByShopParam): CakeImageListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)

        return supplyCakeImageListResponse(cakeImages)
    }

    fun findCakeImagesByCursorAndSearch(dto: CakeSearchParam): CakeImageListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndSearchKeyword(dto)

		dto.keyword?.run {
			val event = IncreaseSearchCountEvent(dto.keyword)
			eventPublisher.publishEvent(event)
		}

        return supplyCakeImageListResponse(cakeImages)
    }

    fun searchCakeImagesByCursorAndViews(dto: CakeSearchByViewsParam): CakeImageListResponse {
        val offset = dto.offset
        val pageSize = dto.pageSize
        val cakeIds: List<Long> = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(offset, pageSize.toLong())

		return when {
			cakeIds.isEmpty() -> {
				supplyCakeImageListResponse(listOf(), cakeIds)
			}

			else -> {
				val cakeImages = cakeReadFacade.searchCakeImagesByCakeIds(cakeIds)
				supplyCakeImageListResponse(cakeImages, cakeIds)
			}
		}
	}

    fun findCakeDetailById(cakeId: Long): CakeDetailResponse {
        val cake = cakeReadFacade.searchCakeDetailById(cakeId)

        return cakeDetailResponseFromParam(cake)
    }

    @Transactional
    fun createCake(param: CakeCreateParam) {
        val cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.cakeShopId, param.owner)
        val cake = param.cake
        val tags = tagReadFacade.getTagsByTagName(param.tagNames)
        val cakeCategories = param.cakeCategories

        cakeManageFacade.create(cakeShop, cake, tags, cakeCategories)
    }

    @Transactional
    fun updateCake(param: CakeUpdateParam) {
        val cake = cakeReadFacade.findWithCakeTagsAndCakeCategories(param.cakeId, param.owner)
        val tags = tagReadFacade.getTagsByTagName(param.tagNames)
        val cakeImageUrl = param.cakeImageUrl
        val cakeCategories = param.cakeCategories

        cakeManageFacade.update(cake, cakeImageUrl, tags, cakeCategories)
    }

    @Transactional
    fun deleteCake(owner: User, cakeId: Long) {
        val cake = cakeReadFacade.findWithCakeTagsAndCakeCategories(cakeId, owner)

        cakeManageFacade.delete(cake)
    }
}
