package com.cakk.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.mapper.UserMapper;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.BusinessInformationWriter;
import com.cakk.domain.mysql.repository.writer.CakeLikeWriter;
import com.cakk.domain.mysql.repository.writer.CakeShopLikeWriter;
import com.cakk.domain.mysql.repository.writer.UserWriter;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserReader userReader;
	private final UserWriter userWriter;
	private final CakeShopLikeWriter cakeShopLikeWriter;
	private final CakeLikeWriter cakeLikeWriter;
	private final BusinessInformationWriter businessInformationWriter;

	@Transactional
	public void updateInformation(final User signInUser, final ProfileUpdateRequest dto) {
		final User user = userReader.findByUserId(signInUser.getId());
		final ProfileUpdateParam param = UserMapper.supplyProfileUpdateParamBy(dto);

		user.updateProfile(param);
	}

	@Transactional
	public void withdraw(final User signInUser) {
		final User user = userReader.findByUserId(signInUser.getId());

		cakeLikeWriter.deleteAllByUser(user);
		cakeShopLikeWriter.deleteAllByUser(user);
		businessInformationWriter.deleteAllByUser(user);

		userWriter.delete(user);
	}
}
