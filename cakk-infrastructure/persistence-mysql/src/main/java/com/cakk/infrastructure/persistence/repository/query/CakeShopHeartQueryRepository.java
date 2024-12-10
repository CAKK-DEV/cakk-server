package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.cake.QCakeEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopHeartEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopOperationEntity.*;
import static com.cakk.infrastructure.persistence.entity.user.QUserEntity.*;
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
			.select(cakeShopHeartEntity.id)
			.from(cakeShopHeartEntity)
			.where(
				ltCakeShopHeartId(cakeShopHeartId),
				isHeart(userId)
			)
			.limit(pageSize)
			.fetch();
	}

	public List<HeartCakeShopResponseParam> searchAllByCursorAndHeart(final List<Long> cakeShopHeartIds) {
		return queryFactory
			.selectFrom(cakeShopHeartEntity)
			.innerJoin(cakeShopEntity)
			.on(cakeShopHeartEntity.cakeShop.eq(cakeShopEntity))
			.innerJoin(userEntity)
			.on(cakeShopHeartEntity.user.eq(userEntity))
			.innerJoin(cakeShopOperationEntity)
			.on(cakeShopEntity.eq(cakeShopOperationEntity.cakeShop))
			.innerJoin(cakeEntity)
			.on(cakeShopEntity.eq(cakeEntity.cakeShop))
			.where(inCakeShopHeartIds(cakeShopHeartIds))
			.orderBy(cakeShopHeartIdDesc())
			.transform(groupBy(cakeShopHeartEntity.id)
				.list(Projections.constructor(HeartCakeShopResponseParam.class,
					cakeShopHeartEntity.id,
					cakeShopEntity.id,
					cakeShopEntity.thumbnailUrl,
					cakeShopEntity.shopName,
					cakeShopEntity.shopBio,
					set(cakeEntity.cakeImageUrl),
					set(cakeShopOperationEntity.operationDay)
				)
				)
			);
	}

	private BooleanExpression ltCakeShopHeartId(Long cakeShopHeartId) {
		if (isNull(cakeShopHeartId)) {
			return null;
		}

		return cakeShopHeartEntity.id.lt(cakeShopHeartId);
	}

	private BooleanExpression inCakeShopHeartIds(List<Long> cakeShopHeartIds) {
		if (isNull(cakeShopHeartIds) || cakeShopHeartIds.isEmpty()) {
			return cakeShopHeartEntity.id.in(List.of());
		}

		return cakeShopHeartEntity.id.in(cakeShopHeartIds);
	}

	private BooleanExpression isHeart(Long userId) {
		return cakeShopHeartEntity.user.id.eq(userId);
	}

	private OrderSpecifier<Long> cakeShopHeartIdDesc() {
		return cakeShopHeartEntity.id.desc();
	}
}
