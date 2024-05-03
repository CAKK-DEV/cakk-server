package com.cakk.api.service.slack;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackService {

	private final SlackApi slackApi;
	private final Environment environment;

	@Value("${slack.webhook.is-enable}")
	private boolean isEnable;

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

		String profile = environment.getProperty("spring.profiles.default");
		SlackMessage slackMessage = new SlackMessage();

		slackMessage.setAttachments(List.of(slackAttachment));
		slackMessage.setChannel("#error-log");
		slackMessage.setUsername("%s API Error".formatted(profile));
		slackMessage.setIcon(":alert:");
		slackMessage.setText("%s api 에러 발생".formatted(profile));

		slackApi.call(slackMessage);
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
}
