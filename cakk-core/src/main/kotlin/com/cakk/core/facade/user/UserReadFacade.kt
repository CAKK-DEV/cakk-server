package com.cakk.core.facade.user

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.repository.jpa.UserJpaRepository
import com.cakk.infrastructure.persistence.repository.query.UserQueryRepository

@DomainFacade
class UserReadFacade(
	private val userJpaRepository: UserJpaRepository,
	private val userQueryRepository: UserQueryRepository
) {

    fun findByUserId(userId: Long): UserEntity {
        return userJpaRepository.findById(userId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_USER) }
	}

    fun findByProviderId(providerId: String): UserEntity {
        return userJpaRepository.findByProviderId(providerId) ?: throw CakkException(ReturnCode.NOT_EXIST_USER)
	}

    fun findByIdWithAll(userId: Long): UserEntity {
		return userQueryRepository.searchByIdWithAll(userId) ?: throw CakkException(ReturnCode.NOT_EXIST_USER)
    }

    fun findAll(): List<UserEntity> {
        return userJpaRepository.findAll()
    }
}

