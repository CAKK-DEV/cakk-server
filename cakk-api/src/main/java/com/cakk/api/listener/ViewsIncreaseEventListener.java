package com.cakk.api.listener;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.domain.mysql.event.views.CakeIncreaseViewsEvent;
import com.cakk.domain.mysql.event.views.CakeShopIncreaseViewsEvent;
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository;
import com.cakk.domain.redis.repository.CakeViewsRedisRepository;

@RequiredArgsConstructor
@ApplicationEventListener
public class ViewsIncreaseEventListener {

	private final CakeViewsRedisRepository cakeViewsRedisRepository;
	private final CakeShopViewsRedisRepository cakeShopViewsRedisRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void increaseCakeViews(final CakeIncreaseViewsEvent event) {
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(Objects.requireNonNull(event.cakeId()));
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void increaseCakeShopViews(final CakeShopIncreaseViewsEvent event) {
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(Objects.requireNonNull(event.cakeShopId()));
	}
}
