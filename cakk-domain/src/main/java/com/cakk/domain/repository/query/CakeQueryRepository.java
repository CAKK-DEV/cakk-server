package com.cakk.domain.repository.query;

import static com.cakk.domain.entity.cake.QCake.*;
import static com.cakk.domain.entity.cake.QCakeCategory.*;
import static com.cakk.domain.entity.shop.QCakeShop.*;
import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.dto.param.cake.CakeImageResponseParam;

@RequiredArgsConstructor
@Repository
public class CakeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CakeImageResponseParam> findCakeImagesByCursorAndCategory(Long cakeId, CakeDesignCategory category, int pageSize) {
		return queryFactory
			.select(Projections.constructor(CakeImageResponseParam.class,
				cakeShop.id,
				cake.id,
				cake.cakeImageUrl))
			.from(cake)
			.innerJoin(cake.cakeShop, cakeShop)
			.innerJoin(cakeCategory.cake, cake)
			.where(
				ltCakeId(cakeId),
				eqCategory(category))
			.limit(pageSize)
			.orderBy(cakeIdDesc())
			.fetch();
	}

	private BooleanExpression ltCakeId(Long cakeId) {
		if (isNull(cakeId)) {
			return null;
		}

		return cake.id.lt(cakeId);
	}

	private BooleanExpression eqCategory(CakeDesignCategory category) {
		return cakeCategory.cakeDesignCategory.eq(category);
	}

	private OrderSpecifier<Long> cakeIdDesc() {
		return cake.id.desc();
	}
}
