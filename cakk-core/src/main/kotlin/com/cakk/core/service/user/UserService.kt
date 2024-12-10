package com.cakk.core.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.supplyProfileInformationResponseBy
import com.cakk.core.mapper.supplyUserWithdrawalBy
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@Service
class UserService(
	private val userReadFacade: UserReadFacade,
	private val userManageFacade: UserManageFacade
) {


	@Transactional(readOnly = true)
	fun findProfile(signInUserEntity: UserEntity): ProfileInformationResponse {
		val user = userReadFacade.findByUserId(signInUserEntity.id)

		return supplyProfileInformationResponseBy(user)
	}

	@Transactional
	fun updateInformation(param: com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam) {
		val user = userReadFacade.findByUserId(param.userId)
		userManageFacade.updateProfile(user, param)
	}

	@Transactional
	fun withdraw(signInUserEntity: UserEntity) {
		val user = userReadFacade.findByIdWithAll(signInUserEntity.id)
		val withdrawal = supplyUserWithdrawalBy(user)

		userManageFacade.withdraw(user, withdrawal)
	}
}
