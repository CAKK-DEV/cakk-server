package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.cake.QCakeCategoryEntity.*;
import static com.cakk.infrastructure.persistence.entity.cake.QCakeEntity.*;
import static com.cakk.infrastructure.persistence.entity.cake.QCakeHeartEntity.*;
import static com.cakk.infrastructure.persistence.entity.cake.QCakeTagEntity.*;
import static com.cakk.infrastructure.persistence.entity.cake.QTagEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopEntity.*;
import static com.cakk.infrastructure.persistence.entity.user.QBusinessInformationEntity.*;
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
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;
import com.cakk.infrastructure.persistence.param.cake.CakeDetailParam;
import com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam;
import com.cakk.infrastructure.persistence.param.tag.TagParam;

@RequiredArgsConstructor
@Repository
public class CakeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CakeImageWithShopInfoResponseParam> searchCakeImagesByCursorAndCategory(
		final Long cakeId,
		final CakeDesignCategory category,
		final int pageSize
	) {
		return queryFactory
			.select(constructor(CakeImageWithShopInfoResponseParam.class,
				cakeShopEntity.id,
				cakeEntity.id,
				cakeEntity.cakeImageUrl,
				cakeEntity.cakeShop.thumbnailUrl,
				cakeEntity.cakeShop.shopName))
			.from(cakeEntity)
			.innerJoin(cakeShopEntity)
			.on(cakeEntity.cakeShop.eq(cakeShopEntity))
			.innerJoin(cakeCategoryEntity)
			.on(cakeCategoryEntity.cake.eq(cakeEntity))
			.where(
				ltCakeId(cakeId),
				eqCategory(category))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageWithShopInfoResponseParam> searchCakeImagesByCursorAndCakeShopId(
		final Long cakeId,
		final Long cakeShopId,
		final int pageSize
	) {
		return queryFactory
			.select(constructor(CakeImageWithShopInfoResponseParam.class,
				cakeShopEntity.id,
				cakeEntity.id,
				cakeEntity.cakeImageUrl,
				cakeEntity.cakeShop.thumbnailUrl,
				cakeEntity.cakeShop.shopName))
			.from(cakeEntity)
			.innerJoin(cakeShopEntity)
			.on(cakeEntity.cakeShop.eq(cakeShopEntity))
			.where(
				ltCakeId(cakeId),
				eqCakeShopId(cakeShopId))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	public List<CakeImageWithShopInfoResponseParam> searchCakeImagesByCursorAndSearchKeyword(
		final Long cakeId,
		final String keyword,
		final Point location,
		final Integer pageSize
	) {
		return queryFactory
			.select(
				constructor(CakeImageWithShopInfoResponseParam.class,
					cakeEntity.cakeShop.id,
					cakeEntity.id,
					cakeEntity.cakeImageUrl,
					cakeEntity.cakeShop.thumbnailUrl,
					cakeEntity.cakeShop.shopName)).distinct()
			.from(cakeEntity)
			.innerJoin(cakeEntity.cakeShop, cakeShopEntity)
			.leftJoin(cakeEntity.cakeCategories, cakeCategoryEntity)
			.leftJoin(cakeEntity.cakeTags, cakeTagEntity)
			.leftJoin(cakeTagEntity.tag, tagEntity)
			.where(
				containKeyword(keyword).and(includeDistance(location)), ltCakeId(cakeId)
			)
			.orderBy(cakeIdDesc())
			.limit(pageSize)
			.fetch();
	}

	public List<CakeImageWithShopInfoResponseParam> searchCakeImagesByCakeIds(final List<Long> cakeIds) {
		return queryFactory
			.select(constructor(CakeImageWithShopInfoResponseParam.class,
				cakeShopEntity.id,
				cakeEntity.id,
				cakeEntity.cakeImageUrl,
				cakeEntity.cakeShop.thumbnailUrl,
				cakeEntity.cakeShop.shopName))
			.from(cakeEntity)
			.innerJoin(cakeShopEntity)
			.on(cakeEntity.cakeShop.eq(cakeShopEntity))
			.where(includeCakeIds(cakeIds))
			.fetch();
	}

	public Optional<CakeEntity> searchWithCakeTagsAndCakeCategories(final Long cakeId, final UserEntity owner) {
		BooleanExpression userCondition = null;

		if (owner.getRole() != Role.ADMIN) {
			userCondition = businessInformationEntity.user.eq(owner)
				.and(businessInformationEntity.verificationStatus.eq(VerificationStatus.APPROVED));
		}

		JPQLQuery<CakeEntity> query = queryFactory
			.selectFrom(cakeEntity)
			.innerJoin(cakeEntity.cakeShop, cakeShopEntity)
			.innerJoin(cakeShopEntity.businessInformation, businessInformationEntity)
			.leftJoin(cakeEntity.cakeCategories, cakeCategoryEntity).fetchJoin()
			.leftJoin(cakeEntity.cakeTags, cakeTagEntity).fetchJoin()
			.where(eqCakeId(cakeId));

		if (nonNull(userCondition)) {
			query.where(userCondition);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	public CakeDetailParam searchCakeDetailById(final Long cakeId) {
		List<CakeDetailParam> results = queryFactory
			.selectFrom(cakeEntity)
			.innerJoin(cakeEntity.cakeShop, cakeShopEntity)
			.leftJoin(cakeEntity.cakeCategories, cakeCategoryEntity)
			.leftJoin(cakeEntity.cakeTags, cakeTagEntity)
			.leftJoin(cakeTagEntity.tag, tagEntity)
			.where(eqCakeId(cakeId))
			.transform(groupBy(cakeEntity.id)
				.list(Projections.constructor(CakeDetailParam.class,
					cakeEntity.cakeImageUrl,
					cakeShopEntity.shopName,
					cakeShopEntity.shopBio,
					cakeShopEntity.id,
					set(cakeCategoryEntity.cakeDesignCategory),
					set(Projections.constructor(TagParam.class,
						tagEntity.id,
						tagEntity.tagName)
					)
				)));
		return results.isEmpty() ? null : results.getFirst();
	}

	public CakeEntity searchByIdWithHeart(final Long cakeId) {
		return queryFactory
			.selectFrom(cakeEntity)
			.leftJoin(cakeEntity.cakeHearts, cakeHeartEntity).fetchJoin()
			.where(cakeEntity.id.eq(cakeId))
			.fetchOne();
	}

	public CakeEntity searchByIdWithCategories(final Long cakeId) {
		return queryFactory
			.selectFrom(cakeEntity)
			.leftJoin(cakeEntity.cakeCategories, cakeCategoryEntity).fetchJoin()
			.where(cakeEntity.id.eq(cakeId))
			.fetchOne();
	}

	private BooleanExpression ltCakeId(final Long cakeId) {
		if (isNull(cakeId)) {
			return null;
		}

		return cakeEntity.id.lt(cakeId);
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShopEntity.id.eq(cakeShopId);
	}

	private BooleanExpression eqCakeId(Long cakeId) {
		return cakeEntity.id.eq(cakeId);
	}

	private BooleanExpression eqCategory(CakeDesignCategory category) {
		return cakeCategoryEntity.cakeDesignCategory.eq(category);
	}

	private BooleanExpression includeCakeIds(List<Long> cakeIds) {
		return cakeEntity.id.in(cakeIds);
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

		return cakeShopEntity.shopBio.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsKeywordInShopDesc(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeShopEntity.shopDescription.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsKeywordInTagName(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTagEntity.tag.tagName.containsIgnoreCase(keyword);
	}

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 1000), {1})", location, cakeShopEntity.location);
	}

	private OrderSpecifier<Long> cakeIdDesc() {
		return cakeEntity.id.desc();
	}
}
