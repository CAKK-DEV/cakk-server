package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShopOperation.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CakeShopOperationQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<ShopOperationParam> findByCakeShopIds(List<Long> cakeShopIds) {
		return queryFactory
			.select(Projections.constructor(ShopOperationParam.class,
				cakeShop.id,
				cakeShopOperation.operationDay,
				cakeShopOperation.operationStartTime,
				cakeShopOperation.operationEndTime))
			.from(cakeShopOperation)
			.innerJoin(cakeShop)
			.on(cakeShopOperation.cakeShop.eq(cakeShop))
			.where(
				includeCakeShopIds(cakeShopIds)
			)
			.orderBy(cakeShopIdsAsc())
			.fetch();
	}

	private BooleanExpression includeCakeShopIds(List<Long> cakeShopIds) {
		return cakeShopOperation.cakeShop.id.in(cakeShopIds);
	}

	private OrderSpecifier<Long> cakeShopIdsAsc() {
		return cakeShopOperation.id.asc();
	}
}
