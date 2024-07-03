package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeHeart.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;

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
				cakeShop.id,
				cake.id,
				cakeHeart.id,
				cake.cakeImageUrl))
			.from(cakeHeart)
			.innerJoin(cake)
			.on(cakeHeart.cake.eq(cake))
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
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

		return cakeHeart.id.lt(cakeHeartId);
	}

	private BooleanExpression isHeart(final Long userId) {
		return cakeHeart.user.id.eq(userId);
	}

	private OrderSpecifier<Long> cakeHeartIdDesc() {
		return cakeHeart.id.desc();
	}
}
