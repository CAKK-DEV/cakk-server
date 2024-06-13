package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeCategory.*;
import static com.cakk.domain.mysql.entity.cake.QCakeTag.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;

@RequiredArgsConstructor
@Repository
public class CakeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCategory(Long cakeId, CakeDesignCategory category, int pageSize) {
		return queryFactory
			.select(Projections.constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.innerJoin(cakeCategory)
			.on(cakeCategory.cake.eq(cake))
			.where(
				ltCakeId(cakeId),
				eqCategory(category))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCakeShopId(Long cakeId, Long cakeShopId, int pageSize) {
		return queryFactory
			.select(Projections.constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.where(
				ltCakeId(cakeId),
				eqCakeShopId(cakeShopId))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCakeShopIds(List<Long> cakeShopIds) {
		return queryFactory
			.select(Projections.constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.where(
				includeCakeShopIds(cakeShopIds)
			)
			.orderBy(cakeLikeCountDesc())
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(
		Long cakeId,
		String keyword,
		Point location,
		Integer pageSize
	) {
		return queryFactory
			.select(
				Projections.constructor(CakeImageResponseParam.class,
					cake.cakeShop.id,
					cake.id,
					cake.cakeImageUrl)).distinct()
			.from(cake)
			.innerJoin(cakeShop)
			.on(cakeShop.eq(cake.cakeShop))
			.leftJoin(cakeTag)
			.on(cakeTag.cake.eq(cake))
			.leftJoin(cakeCategory)
			.on(cakeCategory.cake.eq(cake))
			.where(
				includeDistance(location).and(containKeyword(keyword)), ltCakeId(cakeId)
			)
			.orderBy(cakeIdDesc())
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression ltCakeId(Long cakeId) {
		if (isNull(cakeId)) {
			return null;
		}

		return cake.id.lt(cakeId);
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShop.id.eq(cakeShopId);
	}

	private BooleanExpression eqCategory(CakeDesignCategory category) {
		return cakeCategory.cakeDesignCategory.eq(category);
	}

	private BooleanExpression includeCakeShopIds(List<Long> cakeShopIds) {
		return cake.cakeShop.id.in(cakeShopIds);
	}

	private BooleanBuilder containKeyword(String keyword) {
		BooleanBuilder builder = new BooleanBuilder();

		if (nonNull(keyword)) {
			builder.or(containsKeywordInShopBio(keyword));
			builder.or(containsKeywordInShopDesc(keyword));
			builder.or(containsKeywordInTagName(keyword));
		}

		return builder;
	}

	private BooleanExpression containsKeywordInShopBio(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeShop.shopBio.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsKeywordInShopDesc(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeShop.shopDescription.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsKeywordInTagName(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTag.tag.tagName.containsIgnoreCase(keyword);
	}

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 5000), {1})", location, cakeShop.location);
	}

	private OrderSpecifier<Long> cakeIdDesc() {
		return cake.id.desc();
	}

	private OrderSpecifier<Integer> cakeLikeCountDesc() {
		return cake.likeCount.desc();
	}
}
