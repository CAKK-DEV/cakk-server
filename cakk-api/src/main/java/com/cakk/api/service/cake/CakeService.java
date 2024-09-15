package com.cakk.api.service.cake;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.event.IncreaseSearchCountEvent;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByLocationRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.request.cake.CakeSearchByViewsRequest;
import com.cakk.api.dto.response.cake.CakeDetailResponse;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.core.facade.cake.CakeManageFacade;
import com.cakk.core.facade.cake.CakeReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.tag.TagReadFacade;
import com.cakk.domain.mysql.dto.param.cake.CakeCreateParam;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.cake.CakeUpdateParam;
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

	public CakeImageListResponse findCakeImagesByCursorAndCategory(final CakeSearchByCategoryRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndCakeShopId(final CakeSearchByShopRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndSearch(final CakeSearchByLocationRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReadFacade.searchCakeImagesByCursorAndSearchKeyword(dto.toParam());
		final IncreaseSearchCountEvent event = new IncreaseSearchCountEvent(dto.keyword());

		publisher.publishEvent(event);

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse searchCakeImagesByCursorAndViews(final CakeSearchByViewsRequest dto) {
		final long offset = isNull(dto.offset()) ? 0 : dto.offset();
		final int pageSize = dto.pageSize();
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
