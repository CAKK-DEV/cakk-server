package com.cakk.core.listener

import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

import com.cakk.core.annotation.ApplicationEventListener
import com.cakk.core.dto.event.IncreaseSearchCountEvent
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
