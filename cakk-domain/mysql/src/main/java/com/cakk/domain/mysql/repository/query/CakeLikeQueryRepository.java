package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeLike.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;

@RequiredArgsConstructor
@Repository
public class CakeLikeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<LikeCakeImageResponseParam> searchCakeImagesByCursorAndLike(Long cakeLikeId, Long userId, int pageSize) {
		return queryFactory
			.select(Projections.constructor(LikeCakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cakeLike.id,
				cake.cakeImageUrl))
			.from(cakeLike)
			.innerJoin(cake)
			.on(cakeLike.cake.eq(cake))
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.where(
				ltCakeLikeId(cakeLikeId),
				isLike(userId)
			)
			.limit(pageSize)
			.orderBy(cakeLikeIdDesc())
			.fetch();
	}

	private BooleanExpression ltCakeLikeId(Long cakeLikeId) {
		if (isNull(cakeLikeId)) {
			return null;
		}

		return cakeLike.id.lt(cakeLikeId);
	}

	private BooleanExpression isLike(Long userId) {
		return cakeLike.user.id.eq(userId);
	}

	private OrderSpecifier<Long> cakeLikeIdDesc() {
		return cakeLike.id.desc();
	}
}
