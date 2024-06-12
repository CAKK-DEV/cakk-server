package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShopLink.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShopOperation.*;
import static com.querydsl.core.group.GroupBy.*;
import static java.util.Objects.*;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import com.cakk.domain.mysql.dto.param.shop.CakeShopByKeywordParam;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;

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
					set(cakeShopOperation.operationDay),
					set(Projections.constructor(CakeShopLinkParam.class,
						cakeShopLink.linkKind,
						cakeShopLink.linkPath)
					)
				)));

		return results.isEmpty() ? null : results.get(0);
	}

	public CakeShopSimpleParam searchSimpleById(Long cakeShopId) {
		return queryFactory.select(Projections.constructor(CakeShopSimpleParam.class,
				cakeShop.id,
				cakeShop.thumbnailUrl,
				cakeShop.shopName,
				cakeShop.shopBio
			))
			.from(cakeShop)
			.where(eqCakeShopId(cakeShopId))
			.fetchOne();
	}

	public CakeShopInfoParam searchInfoById(Long cakeShopId) {
		List<CakeShopInfoParam> results = queryFactory
			.selectFrom(cakeShop)
			.innerJoin(cakeShopOperation)
			.on(cakeShop.eq(cakeShopOperation.cakeShop))
			.where(eqCakeShopId(cakeShopId))
			.transform(groupBy(cakeShop.id)
				.list(Projections.constructor(CakeShopInfoParam.class,
					cakeShop.shopAddress,
					cakeShop.location,
					list(Projections.constructor(CakeShopOperationParam.class,
						cakeShopOperation.operationDay,
						cakeShopOperation.operationStartTime,
						cakeShopOperation.operationEndTime)
					)
				)));

		return results.isEmpty() ? null : results.get(0);
	}

	public List<CakeShopByKeywordParam> findByKeywordWithLocation(
		Long cakeShopId,
		String keyword,
		Point location,
		Integer pageSize
	) {
		return queryFactory
			.select(Projections.constructor(CakeShopByKeywordParam.class,
				cakeShop.id,
				cakeShop.thumbnailUrl,
				cakeShop.shopName,
				cakeShop.shopBio
			))
			.where(
				ltCakeShopId(cakeShopId)
					.or(containsKeywordInShopDesc(keyword))
					.or(containsKeywordInShopBio(keyword))
					.or(includeDistance(location))
			)
			.limit(pageSize)
			.orderBy(cakeShopIdDesc())
			.fetch();
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShop.id.eq(cakeShopId);
	}

	private BooleanExpression ltCakeShopId(Long cakeShopId) {
		if (isNull(cakeShopId)) {
			return null;
		}

		return cakeShop.id.lt(cakeShopId);
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

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 5000), {1})", location, cakeShop.location);
	}

	private OrderSpecifier<Long> cakeShopIdDesc() {
		return cakeShop.id.desc();
	}
}
