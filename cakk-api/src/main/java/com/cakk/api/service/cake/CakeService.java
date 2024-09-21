package com.cakk.api.service.cake;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.response.cake.CakeDetailResponse;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.core.dto.event.IncreaseSearchCountEvent;
import com.cakk.core.dto.param.cake.CakeCreateParam;
import com.cakk.core.dto.param.cake.CakeSearchByCategoryParam;
import com.cakk.core.dto.param.cake.CakeSearchByShopParam;
import com.cakk.core.dto.param.cake.CakeSearchByViewsParam;
import com.cakk.core.dto.param.cake.CakeSearchParam;
import com.cakk.core.dto.param.cake.CakeUpdateParam;
import com.cakk.core.facade.cake.CakeManageFacade;
import com.cakk.core.facade.cake.CakeReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.tag.TagReadFacade;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.redis.repository.CakeViewsRedisRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CakeService {

	private final CakeReadFacade cakeReadFacade;
	private final TagReadFacade tagReadFacade;
	private final CakeShopReadFacade cakeShopReadFacade;
	private final CakeViewsRedisRepository cakeViewsRedisRepository;
	private final CakeManageFacade cakeManageFacade;
	private final ApplicationEventPublisher publisher;

	public CakeImageListResponse findCakeImagesByCursorAndCategory(final CakeSearchByCategoryParam dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndCategory(dto.getCakeId(), dto.getCategory(), dto.getPageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndCakeShopId(final CakeSearchByShopParam dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndCakeShopId(dto.getCakeId(), dto.getShopId(), dto.getPageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndSearch(final CakeSearchParam dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndSearchKeyword(dto);

		if (nonNull(dto.getKeyword())) {
			final IncreaseSearchCountEvent event = new IncreaseSearchCountEvent(dto.getKeyword());
			publisher.publishEvent(event);
		}

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse searchCakeImagesByCursorAndViews(final CakeSearchByViewsParam dto) {
		final long offset = dto.getOffset();
		final int pageSize = dto.getPageSize();
		final List<Long> cakeIds = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(offset, pageSize);

		if (isNull(cakeIds) || cakeIds.isEmpty()) {
			return CakeMapper.supplyCakeImageListResponse(List.of(), cakeIds);
		}

		final List<CakeImageResponseParam> cakeImages = cakeReadFacade.searchCakeImagesByCakeIds(cakeIds);
		return CakeMapper.supplyCakeImageListResponse(cakeImages, cakeIds);
	}

	public CakeDetailResponse findCakeDetailById(Long cakeId) {
		final CakeDetailParam cake = cakeReadFacade.searchCakeDetailById(cakeId);

		return CakeMapper.cakeDetailResponseFromParam(cake);
	}

	@Transactional
	public void createCake(CakeCreateParam param) {
		final CakeShop cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.getCakeShopId(), param.getOwner());
		final Cake cake = param.getCake();
		final List<Tag> tags = tagReadFacade.getTagsByTagName(param.getTagNames());
		final List<CakeCategory> cakeCategories = param.getCakeCategories();

		cakeManageFacade.create(cakeShop, cake, tags, cakeCategories);
	}

	@Transactional
	public void updateCake(CakeUpdateParam param) {
		final Cake cake = cakeReadFacade.findWithCakeTagsAndCakeCategories(param.getCakeId(), param.getOwner());
		final List<Tag> tags = tagReadFacade.getTagsByTagName(param.getTagNames());
		final String cakeImageUrl = param.getCakeImageUrl();
		final List<CakeCategory> cakeCategories = param.getCakeCategories();

		cakeManageFacade.update(cake, cakeImageUrl, tags, cakeCategories);
	}

	@Transactional
	public void deleteCake(User owner, Long cakeId) {
		final Cake cake = cakeReadFacade.findWithCakeTagsAndCakeCategories(cakeId, owner);

		cakeManageFacade.delete(cake);
	}
}
