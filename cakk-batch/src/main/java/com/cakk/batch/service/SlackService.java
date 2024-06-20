package com.cakk.batch.service;

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

	public void sendJobStartMessage(final String jobName) {
		if (!isEnable) {
			return;
		}

		final SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("good");
		slackAttachment.setTitle(jobName.toUpperCase());
		slackAttachment.setText("%s Job이 시작되었습니다.".formatted(jobName));
		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("Start Time").setValue(LocalDateTime.now().toString())
			)
		);

		final SlackMessage slackMessage = new SlackMessage();
		slackMessage.setChannel("#log_batch-log");
		slackMessage.setUsername("%s Batch".formatted(profile));
		slackMessage.setIcon(":ghost:");
		slackMessage.setText("%s Start".formatted(jobName));
		slackMessage.setAttachments(List.of(slackAttachment));

		slackApi.call(slackMessage);
	}

	public void sendJobFinishMessage(final String jobName) {
		if (!isEnable) {
			return;
		}

		final SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("good");
		slackAttachment.setTitle(jobName.toUpperCase());
		slackAttachment.setText("%s Job이 종료되었습니다.".formatted(jobName));
		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("End Time").setValue(LocalDateTime.now().toString())
			)
		);

		final SlackMessage slackMessage = new SlackMessage();

		slackMessage.setChannel("#log_batch-log");
		slackMessage.setUsername("%s Batch".formatted(profile));
		slackMessage.setIcon(":ghost:");
		slackMessage.setText("%s Finish".formatted(jobName));
		slackMessage.setAttachments(List.of(slackAttachment));

		slackApi.call(slackMessage);
	}

	public void sendJobFailMessage(final String jobName) {
		if (!isEnable) {
			return;
		}

		final SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("danger");
		slackAttachment.setTitle(jobName.toUpperCase());
		slackAttachment.setText("%s Job에서 에러가 발생했습니다.".formatted(jobName));
		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("Fail Time").setValue(LocalDateTime.now().toString())
			)
		);

		final SlackMessage slackMessage = new SlackMessage();

		slackMessage.setChannel("#log-batch-log");
		slackMessage.setUsername("%s Batch".formatted(profile));
		slackMessage.setIcon(":ghost:");
		slackMessage.setText("%s Fail".formatted(jobName));
		slackMessage.setAttachments(List.of(slackAttachment));

		slackApi.call(slackMessage);
	}
}
