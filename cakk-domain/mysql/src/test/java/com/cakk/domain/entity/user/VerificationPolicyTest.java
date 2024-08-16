package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cakk.common.enums.Role;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.bo.user.VerificationPolicy;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;

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

	@Test
	@DisplayName("사장님 인증된 케이크 샵이 존재할 때, 인증 요청에 실패한다")
	void registerCertificationInformation2() {
		//given
		VerificationPolicy verificationPolicy = getVerificationPolicy();
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.APPROVED);
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when, then
		Assertions.assertThrowsExactly(CakkException.class,
			() -> verificationPolicy.requestCertificationBusinessOwner(businessInformation, param));
	}

	@Test
	@DisplayName("사장님 인증 요청이 진행중일 때, 인증 요청에 실패한다")
	void registerCertificationInformation3() {
		//given
		VerificationPolicy verificationPolicy = getVerificationPolicy();
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.PENDING);
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when, then
		Assertions.assertThrowsExactly(CakkException.class,
			() -> verificationPolicy.requestCertificationBusinessOwner(businessInformation, param));
	}

	@Test
	@DisplayName("사장님 인증 요청이 거절 됐을 때, 인증 요청에 실패한다")
	void registerCertificationInformation4() {
		//given
		VerificationPolicy verificationPolicy = getVerificationPolicy();
		BusinessInformation businessInformation = getBusinessInformationFixtureWithCakeShop(VerificationStatus.REJECTED);
		User user = getUserFixture(Role.USER);
		CertificationParam param = getCertificationParamFixtureWithUser(user);

		//when, then
		Assertions.assertThrowsExactly(CakkException.class,
			() -> verificationPolicy.requestCertificationBusinessOwner(businessInformation, param));
	}
}

