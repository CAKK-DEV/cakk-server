package com.cakk.core.listener

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

import net.gpedro.integrations.slack.SlackMessage

import com.cakk.core.annotation.ApplicationEventListener
import com.cakk.core.mapper.supplyCertificationMessageBy
import com.cakk.domain.mysql.event.shop.CertificationEvent
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.CertificationMessage

@ApplicationEventListener
class CertificationEventListener(
    private val messageTemplate: MessageTemplate,
	private val messageExtractor: MessageExtractor<CertificationMessage, SlackMessage>,
	private val messageSender: MessageSender<SlackMessage>
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendMessageToSlack(certificationEvent: CertificationEvent) {
        val certificationMessage = supplyCertificationMessageBy(certificationEvent)
        messageTemplate.sendMessage(certificationMessage, messageExtractor, messageSender)
    }
}
