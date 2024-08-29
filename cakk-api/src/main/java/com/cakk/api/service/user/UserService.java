package com.cakk.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.api.mapper.UserMapper;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;
import com.cakk.domain.mysql.facade.user.UserCommandFacade;
import com.cakk.domain.mysql.repository.reader.UserReader;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserReader userReader;
	private final UserCommandFacade userCommandFacade;

	@Transactional(readOnly = true)
	public ProfileInformationResponse findProfile(final User signInUser) {
		final User user = userReader.findByUserId(signInUser.getId());

		return UserMapper.supplyProfileInformationResponseBy(user);
	}

	@Transactional
	public void updateInformation(final User signInUser, final ProfileUpdateRequest dto) {
		final User user = userReader.findByUserId(signInUser.getId());
		final ProfileUpdateParam param = UserMapper.supplyProfileUpdateParamBy(dto);

		userCommandFacade.updateProfile(user, param);
	}

	@Transactional
	public void withdraw(final User signInUser) {
		final User user = userReader.findByIdWithAll(signInUser.getId());
		final UserWithdrawal withdrawal = UserMapper.supplyUserWithdrawalBy(user);

		userCommandFacade.withdraw(user, withdrawal);
	}
}
