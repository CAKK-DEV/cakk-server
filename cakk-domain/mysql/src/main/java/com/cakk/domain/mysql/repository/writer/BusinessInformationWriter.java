package com.cakk.domain.mysql.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;

@Writer
@RequiredArgsConstructor
public class BusinessInformationWriter {

	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public void deleteAllByUser(final User user) {
		List<BusinessInformation> businessInformationList = businessInformationJpaRepository.findAllByUser(user);

		if (businessInformationList == null || businessInformationList.isEmpty()) {
			return;
		}

		businessInformationJpaRepository.deleteAllInBatch(businessInformationList);
	}
}
