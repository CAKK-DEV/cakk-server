package com.cakk.core.listener

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

import net.gpedro.integrations.slack.SlackMessage

import com.cakk.core.annotation.ApplicationEventListener
import com.cakk.core.dto.event.ErrorAlertEvent
import com.cakk.core.mapper.supplyErrorAlertMessageBy
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.ErrorAlertMessage

@ApplicationEventListener
class ErrorAlertEventListener(
	private val messageTemplate: MessageTemplate,
	private val messageExtractor: MessageExtractor<ErrorAlertMessage, SlackMessage>,
	private val messageSender: MessageSender<SlackMessage>
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendMessageToSlack(errorAlertEvent: ErrorAlertEvent) {
        val certificationMessage = supplyErrorAlertMessageBy(errorAlertEvent)
        messageTemplate.sendMessage(certificationMessage, messageExtractor, messageSender)
    }
}
