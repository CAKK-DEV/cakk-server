package com.cakk.core.facade.user

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam
import com.cakk.infrastructure.persistence.entity.user.User
import com.cakk.infrastructure.persistence.repository.jpa.UserJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.UserWithdrawalJpaRepository

@DomainFacade
class UserManageFacade(
	private val userJpaRepository: UserJpaRepository,
	private val userWithdrawalJpaRepository: UserWithdrawalJpaRepository
) {

	fun create(user: User): User {
		userJpaRepository.findByProviderId(user.providerId)?.let {
			throw CakkException(ReturnCode.ALREADY_EXIST_USER)
		} ?: return userJpaRepository.save(user)
	}

	fun updateProfile(user: User, param: ProfileUpdateParam) {
		user.updateProfile(param)
	}

	fun withdraw(user: com.cakk.infrastructure.persistence.entity.user.User, withdrawal: com.cakk.infrastructure.persistence.entity.user.UserWithdrawal) {
		user.unHeartAndLikeAll()
		user.businessInformationSet.forEach { it.unLinkBusinessOwner() }

		userWithdrawalJpaRepository.save(withdrawal)
		userJpaRepository.delete(user)
	}
}
