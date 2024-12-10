package com.cakk.core.facade.user

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.entity.user.UserWithdrawalEntity
import com.cakk.infrastructure.persistence.repository.jpa.UserJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.UserWithdrawalJpaRepository

@DomainFacade
class UserManageFacade(
	private val userJpaRepository: UserJpaRepository,
	private val userWithdrawalJpaRepository: UserWithdrawalJpaRepository
) {

	fun create(userEntity: UserEntity): UserEntity {
		userJpaRepository.findByProviderId(userEntity.providerId)?.let {
			throw CakkException(ReturnCode.ALREADY_EXIST_USER)
		} ?: return userJpaRepository.save(userEntity)
	}

	fun updateProfile(userEntity: UserEntity, param: ProfileUpdateParam) {
		userEntity.updateProfile(param)
	}

	fun withdraw(userEntity: UserEntity, withdrawal: UserWithdrawalEntity) {
		userEntity.unHeartAndLikeAll()
		userEntity.businessInformationSet.forEach { it.unLinkBusinessOwner() }

		userWithdrawalJpaRepository.save(withdrawal)
		userJpaRepository.delete(userEntity)
	}
}
