package com.cakk.core.facade.user

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.entity.user.UserWithdrawal
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository
import com.cakk.domain.mysql.repository.jpa.UserWithdrawalJpaRepository

@DomainFacade
class UserManageFacade(
	private val userJpaRepository: UserJpaRepository,
	private val userWithdrawalJpaRepository: UserWithdrawalJpaRepository
) {

	fun create(user: User): User {
		userJpaRepository.findByProviderId(user.providerId) ?: return userJpaRepository.save(user)
		throw CakkException(ReturnCode.ALREADY_EXIST_USER)
	}

	fun updateProfile(user: User, param: ProfileUpdateParam) {
		user.updateProfile(param)
	}

	fun withdraw(user: User, withdrawal: UserWithdrawal) {
		user.unHeartAndLikeAll()
		user.businessInformationSet.forEach { it.unLinkBusinessOwner() }

		userWithdrawalJpaRepository.save(withdrawal)
		userJpaRepository.delete(user)
	}
}
