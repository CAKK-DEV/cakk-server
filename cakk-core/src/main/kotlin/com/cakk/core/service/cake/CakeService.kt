package com.cakk.core.service.cake

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.dto.event.IncreaseSearchCountEvent
import com.cakk.core.dto.param.cake.*
import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.cake.CakeImageWithShopInfoListResponse
import com.cakk.core.facade.cake.CakeManageFacade
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.tag.TagReadFacade
import com.cakk.core.mapper.cakeDetailResponseFromParam
import com.cakk.core.mapper.supplyCakeImageWithShopInfoListResponse
import com.cakk.domain.mysql.entity.user.User

@Transactional(readOnly = true)
@Service
class CakeService(
	private val cakeReadFacade: CakeReadFacade,
	private val tagReadFacade: TagReadFacade,
	private val cakeShopReadFacade: CakeShopReadFacade,
	private val cakeManageFacade: CakeManageFacade,
	private val eventPublisher: ApplicationEventPublisher
) {

    fun findCakeImagesByCursorAndCategory(dto: CakeSearchByCategoryParam): CakeImageWithShopInfoListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)

        return supplyCakeImageWithShopInfoListResponse(cakeImages)
    }

    fun findCakeImagesByCursorAndCakeShopId(dto: CakeSearchByShopParam): CakeImageWithShopInfoListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)

        return supplyCakeImageWithShopInfoListResponse(cakeImages)
    }

    fun findCakeImagesByCursorAndSearch(dto: CakeSearchParam): CakeImageWithShopInfoListResponse {
        val cakeImages = cakeReadFacade.searchCakeImagesByCursorAndSearchKeyword(dto)

		dto.keyword?.run {
			val event = IncreaseSearchCountEvent(dto.keyword)
			eventPublisher.publishEvent(event)
		}

        return supplyCakeImageWithShopInfoListResponse(cakeImages)
    }

    fun searchCakeImagesByCursorAndViews(dto: CakeSearchByViewsParam): CakeImageWithShopInfoListResponse {
        val offset = dto.offset
        val pageSize = dto.pageSize
		val (cakeIds, cakeImages) = cakeReadFacade.searchBestCakeImages(offset, pageSize)

		return supplyCakeImageWithShopInfoListResponse(cakeImages, cakeIds)
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
