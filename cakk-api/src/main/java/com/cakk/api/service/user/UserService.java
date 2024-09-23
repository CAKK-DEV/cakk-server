package com.cakk.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.api.mapper.UserMapper;
import com.cakk.core.facade.user.UserManageFacade;
import com.cakk.core.facade.user.UserReadFacade;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserReadFacade userReadFacade;
	private final UserManageFacade userManageFacade;

	@Transactional(readOnly = true)
	public ProfileInformationResponse findProfile(final User signInUser) {
		final User user = userReadFacade.findByUserId(signInUser.getId());

		return UserMapper.supplyProfileInformationResponseBy(user);
	}

	@Transactional
	public void updateInformation(final ProfileUpdateParam param) {
		final User user = userReadFacade.findByUserId(param.userId());
		userManageFacade.updateProfile(user, param);
	}

	@Transactional
	public void withdraw(final User signInUser) {
		final User user = userReadFacade.findByIdWithAll(signInUser.getId());
		final UserWithdrawal withdrawal = UserMapper.supplyUserWithdrawalBy(user);

		userManageFacade.withdraw(user, withdrawal);
	}
}
