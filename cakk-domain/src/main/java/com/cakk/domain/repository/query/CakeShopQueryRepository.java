package com.cakk.domain.repository.query;

import static com.cakk.domain.entity.shop.QCakeShop.*;
import static com.cakk.domain.entity.shop.QCakeShopLink.*;
import static com.cakk.domain.entity.shop.QCakeShopOperation.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.cakk.common.enums.Days;
import com.cakk.domain.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.dto.param.shop.CakeShopLinkParam;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.dto.param.shop.CakeShopSimpleResponse;

@Repository
@RequiredArgsConstructor
public class CakeShopQueryRepository {

	private final JPAQueryFactory queryFactory;

	public CakeShopDetailParam searchDetailById(Long cakeShopId) {
		List<CakeShopDetailParam> results = queryFactory
			.selectFrom(cakeShop)
			.innerJoin(cakeShopOperation)
			.on(cakeShop.eq(cakeShopOperation.cakeShop))
			.innerJoin(cakeShopLink)
			.on(cakeShop.eq(cakeShopLink.cakeShop))
			.where(eqCakeShopId(cakeShopId))
			.transform(groupBy(cakeShop.id)
				.list(Projections.constructor(CakeShopDetailParam.class,
					cakeShop.id,
					cakeShop.shopName,
					cakeShop.thumbnailUrl,
					cakeShop.shopBio,
					cakeShop.shopDescription,
					list(Projections.constructor(Days.class,
						cakeShopOperation.operationDay)
					),
					list(Projections.constructor(CakeShopLinkParam.class,
						cakeShopLink.linkKind,
						cakeShopLink.linkPath)
					)
				)));

		return results.isEmpty() ? null : results.get(0);
	}

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
