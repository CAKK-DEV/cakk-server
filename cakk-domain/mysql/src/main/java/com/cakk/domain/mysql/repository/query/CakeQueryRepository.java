package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeCategory.*;
import static com.cakk.domain.mysql.entity.cake.QCakeHeart.*;
import static com.cakk.domain.mysql.entity.cake.QCakeTag.*;
import static com.cakk.domain.mysql.entity.cake.QTag.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static com.cakk.domain.mysql.entity.user.QBusinessInformation.*;
import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.Projections.*;
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
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.Role;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.tag.TagParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.user.User;

@RequiredArgsConstructor
@Repository
public class CakeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCategory(
		final Long cakeId,
		final CakeDesignCategory category,
		final int pageSize
	) {
		return queryFactory
			.select(constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.innerJoin(cakeCategory)
			.on(cakeCategory.cake.eq(cake))
			.where(
				ltCakeId(cakeId),
				eqCategory(category))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCakeShopId(
		final Long cakeId,
		final Long cakeShopId,
		final int pageSize
	) {
		return queryFactory
			.select(constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.where(
				ltCakeId(cakeId),
				eqCakeShopId(cakeShopId))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(
		final Long cakeId,
		final String keyword,
		final Point location,
		final Integer pageSize
	) {
		return queryFactory
			.select(
				constructor(CakeImageResponseParam.class,
					cake.cakeShop.id,
					cake.id,
					cake.cakeImageUrl)).distinct()
			.from(cake)
			.innerJoin(cake.cakeShop, cakeShop)
			.leftJoin(cake.cakeCategories, cakeCategory)
			.leftJoin(cake.cakeTags, cakeTag)
			.leftJoin(cakeTag.tag, tag)
			.where(
				containKeyword(keyword).and(includeDistance(location)), ltCakeId(cakeId)
			)
			.orderBy(cakeIdDesc())
			.limit(pageSize)
			.fetch();
	}

	public List<CakeImageResponseParam> searchCakeImagesByCakeIds(final List<Long> cakeIds) {
		return queryFactory
			.select(constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cakeShop)
			.on(cake.cakeShop.eq(cakeShop))
			.where(includeCakeIds(cakeIds))
			.fetch();
	}

	public Optional<Cake> searchWithCakeTagsAndCakeCategories(final Long cakeId, final User owner) {
		BooleanExpression userCondition = null;

		if (owner.getRole() != Role.ADMIN) {
			userCondition = businessInformation.user.eq(owner)
				.and(businessInformation.verificationStatus.eq(VerificationStatus.APPROVED));
		}

		JPQLQuery<Cake> query = queryFactory
			.selectFrom(cake)
			.innerJoin(cake.cakeShop, cakeShop)
			.innerJoin(cakeShop.businessInformation, businessInformation)
			.leftJoin(cake.cakeCategories, cakeCategory).fetchJoin()
			.leftJoin(cake.cakeTags, cakeTag).fetchJoin()
			.where(eqCakeId(cakeId));

		if (nonNull(userCondition)) {
			query.where(userCondition);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	public CakeDetailParam searchCakeDetailById(final Long cakeId) {
		List<CakeDetailParam> results = queryFactory
			.selectFrom(cake)
			.innerJoin(cake.cakeShop, cakeShop)
			.leftJoin(cake.cakeCategories, cakeCategory)
			.leftJoin(cake.cakeTags, cakeTag)
			.leftJoin(cakeTag.tag, tag)
			.where(eqCakeId(cakeId))
			.transform(groupBy(cake.id)
				.list(Projections.constructor(CakeDetailParam.class,
					cake.cakeImageUrl,
					cakeShop.shopName,
					cakeShop.shopBio,
					cakeShop.id,
					set(cakeCategory.cakeDesignCategory),
					set(Projections.constructor(TagParam.class,
						tag.id,
						tag.tagName)
					)
				)));
		return results.isEmpty() ? null : results.getFirst();
	}

	public Cake searchByIdWithHeart(final Long cakeId) {
		return queryFactory
			.selectFrom(cake)
			.leftJoin(cake.cakeHearts, cakeHeart).fetchJoin()
			.where(cake.id.eq(cakeId))
			.fetchOne();
	}

	public Cake searchByIdWithCategories(final Long cakeId) {
		return queryFactory
			.selectFrom(cake)
			.leftJoin(cake.cakeCategories, cakeCategory).fetchJoin()
			.where(cake.id.eq(cakeId))
			.fetchOne();
	}

	private BooleanExpression ltCakeId(final Long cakeId) {
		if (isNull(cakeId)) {
			return null;
		}

		return cake.id.lt(cakeId);
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShop.id.eq(cakeShopId);
	}

	private BooleanExpression eqCakeId(Long cakeId) {
		return cake.id.eq(cakeId);
	}

	private BooleanExpression eqCategory(CakeDesignCategory category) {
		return cakeCategory.cakeDesignCategory.eq(category);
	}

	private BooleanExpression includeCakeIds(List<Long> cakeIds) {
		return cake.id.in(cakeIds);
	}

	private BooleanBuilder containKeyword(String keyword) {
		BooleanBuilder builder = new BooleanBuilder();

		if (nonNull(keyword)) {
			builder.or(containsKeywordInShopBio(keyword));
			builder.or(containsKeywordInShopDesc(keyword));
			builder.or(containsKeywordInTagName(keyword));
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

	private BooleanExpression containsKeywordInTagName(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTag.tag.tagName.containsIgnoreCase(keyword);
	}

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 1000), {1})", location, cakeShop.location);
	}

	private OrderSpecifier<Long> cakeIdDesc() {
		return cake.id.desc();
	}
}
