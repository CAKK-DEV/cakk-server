package com.cakk.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.mapper.UserMapper;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.UserWriter;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserReader userReader;
	private final UserWriter userWriter;

	@Transactional
	public void updateInformation(final User signInUser, final ProfileUpdateRequest dto) {
		final User user = userReader.findByUserId(signInUser.getId());
		final ProfileUpdateParam param = UserMapper.supplyProfileUpdateParamBy(dto);

		user.updateProfile(param);
	}
}
