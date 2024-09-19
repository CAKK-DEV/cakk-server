package com.cakk.api.listener;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.dto.event.IncreaseSearchCountEvent;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

@RequiredArgsConstructor
@ApplicationEventListener
public class SearchEventListener {

	private final KeywordRedisRepository keywordRedisRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void increaseSearchCount(final IncreaseSearchCountEvent event) {
		keywordRedisRepository.saveOrIncreaseSearchCount(Objects.requireNonNull(event.keyword()));
	}
}
