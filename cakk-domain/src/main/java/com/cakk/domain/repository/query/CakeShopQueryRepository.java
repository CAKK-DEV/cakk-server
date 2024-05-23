package com.cakk.domain.repository.query;

import static com.cakk.domain.entity.shop.QCakeShop.*;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.dto.param.shop.CakeShopSimpleResponse;

@Repository
@RequiredArgsConstructor
public class CakeShopQueryRepository {

	private final JPAQueryFactory queryFactory;

	public CakeShopSimpleResponse searchSimpleById(Long cakeShopId) {
		return queryFactory.select(Projections.constructor(CakeShopSimpleResponse.class,
				cakeShop.id,
				cakeShop.thumbnailUrl,
				cakeShop.shopName,
				cakeShop.shopBio
			))
			.from(cakeShop)
			.where(eqCakeShopId(cakeShopId))
			.fetchOne();
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShop.id.eq(cakeShopId);
	}
}
