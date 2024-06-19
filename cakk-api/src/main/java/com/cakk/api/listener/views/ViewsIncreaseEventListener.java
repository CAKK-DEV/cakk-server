package com.cakk.api.listener.views;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.event.views.CakeIncreaseViewsEvent;
import com.cakk.domain.redis.repository.CakeViewRedisRepository;

@RequiredArgsConstructor
@Component
public class ViewsIncreaseEventListener {

	private final CakeViewRedisRepository cakeViewRedisRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void increaseViews(final CakeIncreaseViewsEvent event) {
		cakeViewRedisRepository.saveOrIncreaseSearchCount(event.cakeId());
	}
}
