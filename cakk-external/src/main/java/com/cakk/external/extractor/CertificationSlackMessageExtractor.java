package com.cakk.external.extractor;

import java.util.List;

import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import com.cakk.external.vo.CertificationMessage;

public class CertificationSlackMessageExtractor implements CertificationMessageExtractor<SlackMessage> {

	@Override
	public SlackMessage extract(CertificationMessage certificationMessage) {
		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setColor("good");
		slackAttachment.setFallback("OK");
		slackAttachment.setTitle("Request Certification");

		slackAttachment.setFields(List.of(
			new SlackField().setTitle("요청자 PK").setValue(String.valueOf(certificationMessage.userId())),
			new SlackField().setTitle("요청자 이메일").setValue(certificationMessage.userEmail()),
			new SlackField().setTitle("요청자 비상연락망").setValue(certificationMessage.emergencyContact()),
			new SlackField().setTitle("요청자 신분증 이미지").setValue(certificationMessage.idCardImageUrl()),
			new SlackField().setTitle("요청자 사업자등록증 이미지").setValue(certificationMessage.businessRegistrationImageUrl()),
			new SlackField().setTitle("요청 사항").setValue(certificationMessage.message()),
			new SlackField().setTitle("가게 이름").setValue(certificationMessage.shopName()),
			new SlackField().setTitle("가게 위치 위도").setValue(String.valueOf(certificationMessage.latitude())),
			new SlackField().setTitle("가게 위치 경도").setValue(String.valueOf(certificationMessage.longitude()))
		));

		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setAttachments(List.of(slackAttachment));
		slackMessage.setText("사장님 인증 요청");

		return slackMessage;
	}
}

