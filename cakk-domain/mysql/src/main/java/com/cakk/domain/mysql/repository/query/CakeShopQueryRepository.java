package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShopLink.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShopOperation.*;
import static com.cakk.domain.mysql.entity.user.QBusinessInformation.*;
import static com.cakk.domain.mysql.entity.user.QUser.*;
import static com.querydsl.core.group.GroupBy.*;
import static java.util.Objects.*;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.dto.param.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

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

	public List<CakeShop> findByKeywordWithLocation(
		Long cakeShopId,
		String keyword,
		Point location,
		Integer pageSize
	) {
		return queryFactory
			.selectFrom(cakeShop)
			.innerJoin(cakeShop.businessInformation).fetchJoin()
			.leftJoin(cakeShop.cakes).fetchJoin()
			.leftJoin(cakeShop.cakeShopOperations).fetchJoin()
			.where(
				containKeyword(keyword).and(includeDistance(location)), ltCakeShopId(cakeShopId)
			)
			.orderBy(cakeShopIdDesc())
			.limit(pageSize)
			.fetch();
	}

	public Optional<CakeShop> findWithBusinessInformationAndOwnerById(User owner, Long cakeShopId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(cakeShop)
			.join(cakeShop.businessInformation, businessInformation)
			.join(businessInformation.user, user)
			.where(cakeShop.id.eq(cakeShopId), businessInformation.user.eq(owner))
			.fetchOne());
	}

	public Optional<CakeShop> findWithShopLinks(User owner, Long cakeShopId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(cakeShop)
			.join(cakeShop.cakeShopLinks, cakeShopLink).fetchJoin()
			.join(cakeShop.businessInformation, businessInformation).fetchJoin()
			.where(cakeShop.id.eq(cakeShopId), businessInformation.user.eq(owner))
			.fetchOne());
	}

	public Optional<CakeShop> findWithOperations(User owner, Long cakeShopId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(cakeShop)
			.join(cakeShop.cakeShopOperations, cakeShopOperation).fetchJoin()
			.join(cakeShop.businessInformation, businessInformation).fetchJoin()
			.where(cakeShop.id.eq(cakeShopId), businessInformation.user.eq(owner))
			.fetchOne());
	}

	public List<CakeShopByLocationParam> findShopsByLocationBased(Point location) {
		return queryFactory
			.selectFrom(cakeShop)
			.leftJoin(cake)
			.on(cakeShop.eq(cake.cakeShop))
			.where(includeDistance(location))
			.orderBy(cakeShopIdDesc())
			.transform(groupBy(cakeShop.id)
				.list(Projections.constructor(CakeShopByLocationParam.class,
					cakeShop.id,
					cakeShop.thumbnailUrl,
					cakeShop.shopName,
					cakeShop.shopBio,
					set(Projections.constructor(String.class,
						cake.cakeImageUrl
					)),
					cakeShop.location)));
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

	private BooleanBuilder containKeyword(String keyword) {
		BooleanBuilder builder = new BooleanBuilder();

		if (nonNull(keyword)) {
			builder.or(containsKeywordInShopBio(keyword));
			builder.or(containsKeywordInShopDesc(keyword));
		}

		return builder;
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
