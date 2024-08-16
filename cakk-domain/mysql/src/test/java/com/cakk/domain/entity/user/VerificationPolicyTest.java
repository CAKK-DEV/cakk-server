package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.bo.user.VerificationPolicy;

public class VerificationPolicyTest extends DomainTest {

	@Test
	@DisplayName("인증 정책은 사용자를 케이크샵 주인으로 승격시켜준다")
	void approveToBusinessOwner() {
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		VerificationStatus verificationStatus = verificationPolicy.approveToBusinessOwner();

		assertThat(verificationStatus).isEqualTo(VerificationStatus.APPROVED);
	}

	@Test
	@DisplayName("인증 정책은 사용자의 권한이 BUSINESS_OWNER가 아니고 인증 진행중이라면 케이크샵 주인 후보임을 알려준다")
	void isCandidate() {
		VerificationPolicy verificationPolicy = getVerificationPolicy();

		assertThat(verificationPolicy.isCandidate(VerificationStatus.PENDING)).isTrue();
	}
}

