package com.cakk.infrastructure.persistence.repository.query;

import static com.cakk.infrastructure.persistence.entity.user.QUserEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

	private final JPAQueryFactory queryFactory;

	public UserEntity searchByIdWithAll(final Long userId) {
		return queryFactory.selectFrom(userEntity)
			.leftJoin(userEntity.businessInformationSet).fetchJoin()
			.leftJoin(userEntity.cakeHearts).fetchJoin()
			.leftJoin(userEntity.cakeShopHearts).fetchJoin()
			.leftJoin(userEntity.cakeShopLikes).fetchJoin()
			.where(eqUserId(userId))
			.fetchOne();
	}

	private BooleanExpression eqUserId(final Long userId) {
		return userEntity.id.eq(userId);
	}
}
