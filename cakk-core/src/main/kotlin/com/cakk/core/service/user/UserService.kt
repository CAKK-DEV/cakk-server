package com.cakk.core.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.supplyProfileInformationResponseBy
import com.cakk.core.mapper.supplyUserWithdrawalBy
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam
import com.cakk.domain.mysql.entity.user.User

@Service
class UserService(
	private val userReadFacade: UserReadFacade,
	private val userManageFacade: UserManageFacade
) {


	@Transactional(readOnly = true)
	fun findProfile(signInUser: User): ProfileInformationResponse {
		val user = userReadFacade.findByUserId(signInUser.id)

		return supplyProfileInformationResponseBy(user)
	}

	@Transactional
	fun updateInformation(param: ProfileUpdateParam) {
		val user = userReadFacade.findByUserId(param.userId)
		userManageFacade.updateProfile(user, param)
	}

	@Transactional
	fun withdraw(signInUser: User) {
		val user = userReadFacade.findByIdWithAll(signInUser.id)
		val withdrawal = supplyUserWithdrawalBy(user)

		userManageFacade.withdraw(user, withdrawal)
	}
}
