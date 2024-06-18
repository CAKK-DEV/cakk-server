package com.cakk.api.service.cake;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByLocationRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.request.cake.CakeSearchByViewsRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.cake.CakeUpdateParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.reader.TagReader;
import com.cakk.domain.mysql.repository.writer.CakeWriter;
import com.cakk.domain.mysql.repository.writer.TagWriter;
import com.cakk.domain.redis.repository.CakeViewRedisRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CakeService {

	private final CakeReader cakeReader;
	private final CakeWriter cakeWriter;
	private final TagReader tagReader;
	private final TagWriter tagWriter;
	private final CakeViewRedisRepository cakeViewRedisRepository;

	public CakeImageListResponse findCakeImagesByCursorAndCategory(final CakeSearchByCategoryRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReader.searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndCakeShopId(final CakeSearchByShopRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReader.searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse findCakeImagesByCursorAndSearch(final CakeSearchByLocationRequest dto) {
		final List<CakeImageResponseParam> cakeImages
			= cakeReader.searchCakeImagesByCursorAndSearchKeyword(dto.toParam());

		return CakeMapper.supplyCakeImageListResponse(cakeImages);
	}

	public CakeImageListResponse searchCakeImagesByCursorAndViews(final CakeSearchByViewsRequest dto) {
		final long offset = isNull(dto.offset()) ? 0 : dto.offset();
		final int pageSize = dto.pageSize();
		final List<Long> cakeIds = cakeViewRedisRepository.findTopCakeIdsByOffsetAndCount(offset, pageSize);

		if (isNull(cakeIds) || cakeIds.isEmpty()) {
			return CakeMapper.supplyCakeImageListResponse(List.of(), cakeIds);
		}

		final List<CakeImageResponseParam> cakeImages = cakeReader.searchCakeImagesByCakeIds(cakeIds);
		return CakeMapper.supplyCakeImageListResponse(cakeImages, cakeIds);
	}

	@Transactional
	public void updateCake(CakeUpdateParam param) {
		final Cake cake = cakeReader.findWithCakeTagsAndCakeCategories(param.cakeId(), param.owner());
		List<Tag> tags = param.tagNames()
			.stream()
			.map(tagName -> tagReader.findByTagName(tagName).orElseGet(() -> tagWriter.saveTag(tagName)))
			.toList();

		cake.updateCakeImageUrl(param.cakeImageUrl());
		cake.updateCakeCategories(param.cakeCategories());
		cake.updateCakeTags(tags);
	}

	@Transactional
	public void deleteCake(User owner, Long cakeId) {
		final Cake cake = cakeReader.findWithCakeTagsAndCakeCategories(cakeId, owner);

		cake.removeCakeCategories();
		cake.removeCakeTags();
		cakeWriter.deleteCake(cake);
	}
}
