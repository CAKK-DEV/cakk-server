package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeCategory.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
			.innerJoin(cakeCategory)
			.on(cakeCategory.cake.eq(cake))
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

	private OrderSpecifier<Long> cakeIdDesc() {
		return cake.id.desc();
	}

	private OrderSpecifier<Integer> cakeLikeCountDesc() {
		return cake.likeCount.desc();
	}
}
