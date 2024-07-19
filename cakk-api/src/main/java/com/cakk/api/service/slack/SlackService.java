package com.cakk.api.service.slack;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SlackService {

	private final SlackApi slackApi;

	private final String profile;
	private final boolean isEnable;

	public SlackService(
		SlackApi slackApi,
		@Value("${spring.profiles.active}")
		String profile,
		@Value("${slack.webhook.is-enable}")
		boolean isEnable
	) {
		this.slackApi = slackApi;
		this.profile = profile;
		this.isEnable = isEnable;
	}

	public void sendSlackForError(Exception exception, HttpServletRequest request) {
		if (!isEnable) {
			return;
		}

		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("danger");
		slackAttachment.setTitle("Error Detect");
		slackAttachment.setTitleLink(request.getContextPath());
		slackAttachment.setText(Arrays.toString(exception.getStackTrace()));
		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("Request URL").setValue(request.getRequestURL().toString()),
				new SlackField().setTitle("Request Method").setValue(request.getMethod()),
				new SlackField().setTitle("Request Parameter").setValue(getRequestParameters(request)),
				new SlackField().setTitle("Request Time").setValue(LocalDateTime.now().toString()),
				new SlackField().setTitle("Request IP").setValue(request.getRemoteAddr()),
				new SlackField().setTitle("Request User-Agent").setValue(request.getHeader("User-Agent"))
			)
		);

		SlackMessage slackMessage = new SlackMessage();

		slackMessage.setAttachments(List.of(slackAttachment));
		slackMessage.setChannel("#log_server-error");
		slackMessage.setUsername("%s API Error".formatted(profile));
		slackMessage.setIcon(":alert:");
		slackMessage.setText("%s api 에러 발생".formatted(profile));

		slackApi.call(slackMessage);
	}

	public void sendSlackForCertification(CertificationEvent certificationEvent) {
		if (!isEnable) {
			return;
		}

		SlackMessage slackMessage;

		try {
			slackMessage = messageExtractor(certificationEvent, SlackMessage.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		send(slackMessage);
	}

	private String getRequestParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();

			sb.append("Parameter Name: ").append(key);

			for (String value : values) {
				sb.append("Parameter Value: ").append(value);
			}
		}

		return sb.toString();
	}

	private <T> T messageExtractor(CertificationEvent certificationEvent, Class<T> returnType) throws
		JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		SlackMessage slackMessage;
		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setColor("good");
		slackAttachment.setFallback("OK");
		slackAttachment.setTitle("Request Certification");

		slackAttachment.setFields(List.of(
			new SlackField().setTitle("요청자 PK").setValue(String.valueOf(certificationEvent.userId())),
			new SlackField().setTitle("요청자 이메일").setValue(certificationEvent.userEmail()),
			new SlackField().setTitle("요청자 비상연락망").setValue(certificationEvent.emergencyContact()),
			new SlackField().setTitle("요청자 신분증 이미지").setValue(certificationEvent.idCardImageUrl()),
			new SlackField().setTitle("요청자 사업자등록증 이미지").setValue(certificationEvent.businessRegistrationImageUrl()),
			new SlackField().setTitle("요청 사항").setValue(certificationEvent.message()),
			new SlackField().setTitle("가게 이름").setValue(certificationEvent.shopName()),
			new SlackField().setTitle("가게 위치 위도").setValue(String.valueOf(certificationEvent.location().getY())),
			new SlackField().setTitle("가게 위치 경도").setValue(String.valueOf(certificationEvent.location().getX()))
		));

		slackMessage = new SlackMessage();
		slackMessage.setAttachments(List.of(slackAttachment));
		slackMessage.setChannel("#cs_사장님인증");
		slackMessage.setText("%s 사장님 인증 요청".formatted(profile));

		return objectMapper.convertValue(slackMessage, returnType);
	}

	private void send(SlackMessage slackMessage) {
		slackApi.call(slackMessage);
	}
}
