package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.cake.QCake.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShop.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopHeart.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopOperation.*;
import static com.cakk.infrastructure.persistence.entity.user.QUser.*;
import static com.querydsl.core.group.GroupBy.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam;

@RequiredArgsConstructor
@Repository
public class CakeShopHeartQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Long> searchIdsByCursorAndHeart(
		final Long cakeShopHeartId,
		final Long userId,
		final int pageSize
	) {
		return queryFactory
			.select(cakeShopHeart.id)
			.from(cakeShopHeart)
			.where(
				ltCakeShopHeartId(cakeShopHeartId),
				isHeart(userId)
			)
			.limit(pageSize)
			.fetch();
	}

	public List<HeartCakeShopResponseParam> searchAllByCursorAndHeart(final List<Long> cakeShopHeartIds) {
		return queryFactory
			.selectFrom(cakeShopHeart)
			.innerJoin(cakeShop)
			.on(cakeShopHeart.cakeShop.eq(cakeShop))
			.innerJoin(user)
			.on(cakeShopHeart.user.eq(user))
			.innerJoin(cakeShopOperation)
			.on(cakeShop.eq(cakeShopOperation.cakeShop))
			.innerJoin(cake)
			.on(cakeShop.eq(cake.cakeShop))
			.where(inCakeShopHeartIds(cakeShopHeartIds))
			.orderBy(cakeShopHeartIdDesc())
			.transform(groupBy(cakeShopHeart.id)
				.list(Projections.constructor(HeartCakeShopResponseParam.class,
					cakeShopHeart.id,
					cakeShop.id,
					cakeShop.thumbnailUrl,
					cakeShop.shopName,
					cakeShop.shopBio,
					set(cake.cakeImageUrl),
					set(cakeShopOperation.operationDay)
				)
				)
			);
	}

	private BooleanExpression ltCakeShopHeartId(Long cakeShopHeartId) {
		if (isNull(cakeShopHeartId)) {
			return null;
		}

		return cakeShopHeart.id.lt(cakeShopHeartId);
	}

	private BooleanExpression inCakeShopHeartIds(List<Long> cakeShopHeartIds) {
		if (isNull(cakeShopHeartIds) || cakeShopHeartIds.isEmpty()) {
			return cakeShopHeart.id.in(List.of());
		}

		return cakeShopHeart.id.in(cakeShopHeartIds);
	}

	private BooleanExpression isHeart(Long userId) {
		return cakeShopHeart.user.id.eq(userId);
	}

	private OrderSpecifier<Long> cakeShopHeartIdDesc() {
		return cakeShopHeart.id.desc();
	}
}
