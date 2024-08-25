package com.cakk.domain.mysql.repository.reader;

import static java.util.Objects.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.cake.CakeSearchParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeReader {

	private final CakeJpaRepository cakeJpaRepository;
	private final CakeQueryRepository cakeQueryRepository;

	public Cake findById(final Long cakeId) {
		return cakeJpaRepository.findById(cakeId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE));
	}

	public Cake findByIdWithHeart(final Long cakeId) {
		final Cake cake = cakeQueryRepository.searchByIdWithHeart(cakeId);

		if (isNull(cake)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE);
		}

		return cake;
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCategory(
		final Long cakeId,
		final CakeDesignCategory category,
		final int pageSize
	) {
		return cakeQueryRepository.searchCakeImagesByCursorAndCategory(cakeId, category, pageSize);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCakeShopId(
		final Long cakeId,
		final Long cakeShopId,
		final int pageSize
	) {
		return cakeQueryRepository.searchCakeImagesByCursorAndCakeShopId(cakeId, cakeShopId, pageSize);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(final CakeSearchParam param) {
		return cakeQueryRepository.searchCakeImagesByCursorAndSearchKeyword(
			param.cakeId(),
			param.keyword(),
			param.location(),
			param.pageSize()
		);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCakeIds(final List<Long> cakeIds) {
		return cakeQueryRepository.searchCakeImagesByCakeIds(cakeIds);
	}

	public Cake findWithCakeTagsAndCakeCategories(final Long cakeId, final User owner) {
		return cakeQueryRepository.searchWithCakeTagsAndCakeCategories(cakeId, owner)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER));
	}

	public CakeDetailParam searchCakeDetailById(final Long cakeId) {
		final CakeDetailParam param = cakeQueryRepository.searchCakeDetailById(cakeId);

		if (isNull(param)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE);
		}
		return param;
	}
}
