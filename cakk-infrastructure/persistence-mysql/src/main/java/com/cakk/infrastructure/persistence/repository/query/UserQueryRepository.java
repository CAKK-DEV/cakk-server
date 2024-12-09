package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.user.QUser.*;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.persistence.entity.user.User;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

	private final JPAQueryFactory queryFactory;

	public User searchByIdWithAll(final Long userId) {
		return queryFactory.selectFrom(user)
			.leftJoin(user.businessInformationSet).fetchJoin()
			.leftJoin(user.cakeHearts).fetchJoin()
			.leftJoin(user.cakeShopHearts).fetchJoin()
			.leftJoin(user.cakeShopLikes).fetchJoin()
			.where(eqUserId(userId))
			.fetchOne();
	}

	private BooleanExpression eqUserId(final Long userId) {
		return user.id.eq(userId);
	}
}
