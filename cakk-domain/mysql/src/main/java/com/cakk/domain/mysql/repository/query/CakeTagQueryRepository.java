package com.cakk.domain.mysql.repository.query;

import static com.cakk.domain.mysql.entity.cake.QCake.*;
import static com.cakk.domain.mysql.entity.cake.QCakeCategory.*;
import static com.cakk.domain.mysql.entity.cake.QCakeTag.*;
import static com.cakk.domain.mysql.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;

@Repository
@RequiredArgsConstructor
public class CakeTagQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(Long cakeId, String keyword,
		Point location, Integer pageSize) {
		return queryFactory
			.select(Projections.constructor(CakeImageResponseParam.class,
				cakeTag.cake.cakeShop.id,
				cakeTag.cake.id,
				cakeTag.cake.cakeImageUrl))
			.from(cakeTag)
			.innerJoin(cake)
			.on(cakeTag.cake.eq(cake))
			.innerJoin(cakeCategory)
			.on(cakeCategory.cake.eq(cake))
			.where(
				ltCakeId(cakeId)
					.or(containsSearchTextInShopBio(keyword))
					.or(containsSearchTextInShopDesc(keyword))
					.or(containsSearchTextInTagName(keyword))
					.or(includeDistance(location))
			)
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	private BooleanExpression ltCakeId(Long cakeId) {
		if (isNull(cakeId)) {
			return null;
		}

		return cake.id.gt(cakeId);
	}

	private BooleanExpression containsSearchTextInShopBio(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTag.cake.cakeShop.shopBio.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsSearchTextInShopDesc(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTag.cake.cakeShop.shopDescription.containsIgnoreCase(keyword);
	}

	private BooleanExpression containsSearchTextInTagName(String keyword) {
		if (isNull(keyword)) {
			return null;
		}

		return cakeTag.tag.tagName.containsIgnoreCase(keyword);
	}

	private BooleanExpression includeDistance(Point location) {
		if (isNull(location)) {
			return null;
		}

		return Expressions.booleanTemplate("ST_Contains(ST_BUFFER({0}, 5000), {1})", location, cakeShop.location);
	}

	private OrderSpecifier<Long> cakeIdDesc() {
		return cake.id.desc();
	}
}

