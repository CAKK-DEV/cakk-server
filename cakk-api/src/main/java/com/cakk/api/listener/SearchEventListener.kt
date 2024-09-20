package com.cakk.api.listener

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

import com.cakk.api.annotation.ApplicationEventListener
import com.cakk.api.dto.event.IncreaseSearchCountEvent
import com.cakk.domain.redis.repository.KeywordRedisRepository

@ApplicationEventListener
class SearchEventListener(
	private val keywordRedisRepository: KeywordRedisRepository
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun increaseSearchCount(event: IncreaseSearchCountEvent) {
        keywordRedisRepository.saveOrIncreaseSearchCount(event.keyword)
    }
}
