package com.cakk.domain.mysql.facade.user;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository;
import com.cakk.domain.mysql.repository.jpa.UserWithdrawalJpaRepository;

@RequiredArgsConstructor
@DomainFacade
public class UserCommandFacade {

	private final UserJpaRepository userJpaRepository;
	private final UserWithdrawalJpaRepository userWithdrawalJpaRepository;

	public User create(final User user) {
		userJpaRepository.findByProviderId(user.getProviderId())
			.ifPresent(it -> {
				throw new CakkException(ReturnCode.ALREADY_EXIST_USER);
			});

		return userJpaRepository.save(user);
	}

	public void updateProfile(final User user, final ProfileUpdateParam param) {
		user.updateProfile(param);
	}

	public void withdraw(final User user, final UserWithdrawal withdrawal) {
		user.unHeartAndLikeAll();
		user.getBusinessInformationSet().forEach(BusinessInformation::unLinkBusinessOwner);

		userWithdrawalJpaRepository.save(withdrawal);
		userJpaRepository.delete(user);
	}
}
