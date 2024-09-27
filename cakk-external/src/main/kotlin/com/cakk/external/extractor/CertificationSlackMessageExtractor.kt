package com.cakk.external.extractor

import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage

import com.cakk.external.vo.message.CertificationMessage

class CertificationSlackMessageExtractor : SlackMessageExtractor<CertificationMessage> {

    override fun extract(message: CertificationMessage): SlackMessage {
        val slackAttachment = SlackAttachment()
        slackAttachment.setColor("good")
        slackAttachment.setFallback("OK")
        slackAttachment.setTitle("Request Certification")

        slackAttachment.setFields(
            listOf(
                SlackField().setTitle("요청자 PK").setValue(message.userId.toString()),
                SlackField().setTitle("요청자 이메일").setValue(message.userEmail),
                SlackField().setTitle("요청자 비상연락망").setValue(message.emergencyContact),
                SlackField().setTitle("요청자 신분증 이미지").setValue(message.idCardImageUrl),
                SlackField().setTitle("요청자 사업자등록증 이미지").setValue(message.businessRegistrationImageUrl),
                SlackField().setTitle("요청 사항").setValue(message.message),
                SlackField().setTitle("가게 이름").setValue(message.shopName),
                SlackField().setTitle("가게 위치 위도").setValue(message.latitude.toString()),
                SlackField().setTitle("가게 위치 경도").setValue(message.longitude.toString())
            )
        )

        val slackMessage = SlackMessage()
        slackMessage.setAttachments(listOf(slackAttachment))
        slackMessage.setText("사장님 인증 요청")
        slackMessage.setChannel("#cs_사장님인증")

        return slackMessage
    }
}

