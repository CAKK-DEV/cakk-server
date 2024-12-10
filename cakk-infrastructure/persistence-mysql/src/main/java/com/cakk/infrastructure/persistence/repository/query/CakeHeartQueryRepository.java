package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.cake.QCakeEntity.*;
import static com.cakk.infrastructure.persistence.entity.cake.QCakeHeartEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopEntity.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam;

@RequiredArgsConstructor
@Repository
public class CakeHeartQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<HeartCakeImageResponseParam> searchCakeImagesByCursorAndHeart(
		final Long cakeHeartId,
		final Long userId,
		final int pageSize
	) {
		return queryFactory
			.select(Projections.constructor(HeartCakeImageResponseParam.class,
				cakeShopEntity.id,
				cakeEntity.id,
				cakeHeartEntity.id,
				cakeEntity.cakeImageUrl))
			.from(cakeHeartEntity)
			.innerJoin(cakeEntity)
			.on(cakeHeartEntity.cake.eq(cakeEntity))
			.innerJoin(cakeShopEntity)
			.on(cakeEntity.cakeShop.eq(cakeShopEntity))
			.where(
				ltCakeHeartId(cakeHeartId),
				isHeart(userId)
			)
			.limit(pageSize)
			.orderBy(cakeHeartIdDesc())
			.fetch();
	}

	private BooleanExpression ltCakeHeartId(final Long cakeHeartId) {
		if (isNull(cakeHeartId)) {
			return null;
		}

		return cakeHeartEntity.id.lt(cakeHeartId);
	}

	private BooleanExpression isHeart(final Long userId) {
		return cakeHeartEntity.user.id.eq(userId);
	}

	private OrderSpecifier<Long> cakeHeartIdDesc() {
		return cakeHeartEntity.id.desc();
	}
}
