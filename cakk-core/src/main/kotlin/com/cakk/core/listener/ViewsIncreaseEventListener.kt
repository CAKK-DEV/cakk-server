package com.cakk.core.listener


import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

import com.cakk.core.annotation.ApplicationEventListener
import com.cakk.core.dto.event.CakeIncreaseViewsEvent
import com.cakk.core.dto.event.CakeShopIncreaseViewsEvent
import com.cakk.infrastructure.cache.repository.CakeShopViewsRedisRepository
import com.cakk.infrastructure.cache.repository.CakeViewsRedisRepository

@ApplicationEventListener
class ViewsIncreaseEventListener(
    private val cakeViewsRedisRepository: CakeViewsRedisRepository,
    private val cakeShopViewsRedisRepository: CakeShopViewsRedisRepository
) {

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	fun increaseCakeViews(event: CakeIncreaseViewsEvent) {
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(event.cakeId)
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	fun increaseCakeShopViews(event: CakeShopIncreaseViewsEvent) {
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(event.cakeShopId)
	}
}
