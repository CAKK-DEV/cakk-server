package com.cakk.domain.mysql.repository.query;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CakeTagQueryRepository {

	private final JPAQueryFactory queryFactory;
}

