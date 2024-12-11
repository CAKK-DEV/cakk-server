package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.cake.QCakeEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopLinkEntity.*;
import static com.cakk.infrastructure.persistence.entity.shop.QCakeShopOperationEntity.*;
import static com.cakk.infrastructure.persistence.entity.user.QBusinessInformationEntity.*;
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
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.Role;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.infrastructure.persistence.bo.shop.CakeShopByLocationParam;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;
import com.cakk.infrastructure.persistence.param.shop.CakeShopDetailParam;
import com.cakk.infrastructure.persistence.param.shop.CakeShopInfoParam;
import com.cakk.infrastructure.persistence.param.shop.CakeShopLinkParam;
import com.cakk.infrastructure.persistence.param.shop.CakeShopOperationParam;
import com.cakk.infrastructure.persistence.param.shop.CakeShopSimpleParam;

@Repository
@RequiredArgsConstructor
public class CakeShopQueryRepository {

	private final JPAQueryFactory queryFactory;

	public CakeShopDetailParam searchDetailById(Long cakeShopId) {
		List<CakeShopDetailParam> results = queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeShopEntity.cakeShopOperations, cakeShopOperationEntity)
			.leftJoin(cakeShopEntity.cakeShopLinks, cakeShopLinkEntity)
			.where(eqCakeShopId(cakeShopId))
			.transform(groupBy(cakeShopEntity.id)
				.list(Projections.constructor(CakeShopDetailParam.class,
					cakeShopEntity.id,
					cakeShopEntity.shopName,
					cakeShopEntity.thumbnailUrl,
					cakeShopEntity.shopBio,
					cakeShopEntity.shopDescription,
					set(cakeShopOperationEntity.operationDay),
					set(Projections.constructor(CakeShopLinkParam.class,
						cakeShopLinkEntity.linkKind,
						cakeShopLinkEntity.linkPath)
					)
				)));

		return results.isEmpty() ? null : results.get(0);
	}

	public CakeShopSimpleParam searchSimpleById(Long cakeShopId) {
		return queryFactory.select(Projections.constructor(CakeShopSimpleParam.class,
				cakeShopEntity.id,
				cakeShopEntity.thumbnailUrl,
				cakeShopEntity.shopName,
				cakeShopEntity.shopBio
			))
			.from(cakeShopEntity)
			.where(eqCakeShopId(cakeShopId))
			.fetchOne();
	}

	public CakeShopInfoParam searchInfoById(Long cakeShopId) {
		List<CakeShopInfoParam> results = queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeShopEntity.cakeShopOperations, cakeShopOperationEntity)
			.where(eqCakeShopId(cakeShopId))
			.transform(groupBy(cakeShopEntity.id)
				.list(Projections.constructor(CakeShopInfoParam.class,
					cakeShopEntity.shopAddress,
					cakeShopEntity.location,
					list(Projections.constructor(CakeShopOperationParam.class,
						cakeShopOperationEntity.operationDay,
						cakeShopOperationEntity.operationStartTime,
						cakeShopOperationEntity.operationEndTime)
					)
				)));

		return results.isEmpty() ? null : results.getFirst();
	}

	public List<CakeShopEntity> searchByKeywordWithLocation(
		Long cakeShopId,
		String keyword,
		Point location,
		Integer pageSize
	) {
		return queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeShopEntity.businessInformation).fetchJoin()
			.where(
				ltCakeShopId(cakeShopId),
				includeDistance(location),
				containKeyword(keyword)
			)
			.orderBy(cakeShopIdDesc())
			.limit(pageSize)
			.fetch();
	}

	public Optional<CakeShopEntity> searchWithBusinessInformationAndOwnerById(UserEntity owner, Long cakeShopId) {
		BooleanExpression userCondition = null;

		if (owner.getRole() != Role.ADMIN) {
			userCondition = businessInformationEntity.user.eq(owner)
				.and(businessInformationEntity.verificationStatus.eq(VerificationStatus.APPROVED));
		}
		JPQLQuery<CakeShopEntity> query = queryFactory
			.selectFrom(cakeShopEntity)
			.innerJoin(cakeShopEntity.businessInformation, businessInformationEntity).fetchJoin()
			.where(cakeShopEntity.id.eq(cakeShopId));

		if (nonNull(userCondition)) {
			query.where(userCondition);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	public Optional<CakeShopEntity> searchWithShopLinks(UserEntity owner, Long cakeShopId) {
		BooleanExpression userCondition = null;

		if (owner.getRole() != Role.ADMIN) {
			userCondition = businessInformationEntity.user.eq(owner)
				.and(businessInformationEntity.verificationStatus.eq(VerificationStatus.APPROVED));
		}

		JPQLQuery<CakeShopEntity> query = queryFactory
			.selectFrom(cakeShopEntity)
			.join(cakeShopEntity.cakeShopLinks, cakeShopLinkEntity).fetchJoin()
			.join(cakeShopEntity.businessInformation, businessInformationEntity).fetchJoin()
			.where(cakeShopEntity.id.eq(cakeShopId));

		if (nonNull(userCondition)) {
			query.where(userCondition);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	public Optional<CakeShopEntity> searchWithOperations(UserEntity owner, Long cakeShopId) {
		BooleanExpression userCondition = null;

		if (owner.getRole() != Role.ADMIN) {
			userCondition = businessInformationEntity.user.eq(owner)
				.and(businessInformationEntity.verificationStatus.eq(VerificationStatus.APPROVED));
		}

		JPQLQuery<CakeShopEntity> query = queryFactory
			.selectFrom(cakeShopEntity)
			.join(cakeShopEntity.cakeShopOperations, cakeShopOperationEntity).fetchJoin()
			.join(cakeShopEntity.businessInformation, businessInformationEntity).fetchJoin()
			.where(cakeShopEntity.id.eq(cakeShopId));

		if (nonNull(userCondition)) {
			query.where(userCondition);
		}

		return Optional.ofNullable(query.fetchOne());
	}

	public List<CakeShopByLocationParam> findShopsByLocationBased(final Point location, final Double distance) {
		return queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeEntity)
			.on(cakeShopEntity.eq(cakeEntity.cakeShop))
			.where(includeDistance(location, distance))
			.orderBy(cakeShopIdDesc())
			.transform(groupBy(cakeShopEntity.id)
				.list(Projections.constructor(CakeShopByLocationParam.class,
					cakeShopEntity.id,
					cakeShopEntity.thumbnailUrl,
					cakeShopEntity.shopName,
					cakeShopEntity.shopBio,
					set(cakeEntity.cakeImageUrl.coalesce("")),
					cakeShopEntity.location)));
	}

	public List<CakeShopEntity> searchByShopIds(List<Long> shopIds) {
		return queryFactory
			.selectFrom(cakeShopEntity)
			.innerJoin(cakeShopEntity.businessInformation).fetchJoin()
			.leftJoin(cakeShopEntity.cakes).fetchJoin()
			.leftJoin(cakeShopEntity.cakeShopOperations).fetchJoin()
			.where(includeShopIds(shopIds))
			.fetch();
	}

	public CakeShopEntity searchByIdWithHeart(Long cakeShopId) {
		return queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeShopEntity.shopHearts).fetchJoin()
			.where(cakeShopEntity.id.eq(cakeShopId))
			.fetchOne();
	}

	public CakeShopEntity searchByIdWithLike(Long cakeShopId) {
		return queryFactory
			.selectFrom(cakeShopEntity)
			.leftJoin(cakeShopEntity.shopLikes).fetchJoin()
			.where(cakeShopEntity.id.eq(cakeShopId))
			.fetchOne();
	}

	private BooleanExpression eqCakeShopId(Long cakeShopId) {
		return cakeShopEntity.id.eq(cakeShopId);
	}

	private BooleanExpression includeShopIds(List<Long> shopIds) {
		return cakeShopEntity.id.in(shopIds);
	}

	private BooleanExpression ltCakeShopId(Long cakeShopId) {
		if (isNull(cakeShopId)) {
			return null;
		}

		return cakeShopEntity.id.lt(cakeShopId);
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

		return cakeShopEntity.shopBio.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsKeywordInShopDesc(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeShopEntity.shopDescription.containsIgnoreCase(keyword);
	}

	private BooleanExpression includeDistance(Point location, Double distance) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, {1}), {2})", location, distance, cakeShopEntity.location);
	}

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 1000), {1})", location, cakeShopEntity.location);
	}

	private OrderSpecifier<Long> cakeShopIdDesc() {
		return cakeShopEntity.id.desc();
	}
}
