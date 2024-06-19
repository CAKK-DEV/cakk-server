package com.cakk.api.service.views;

import static java.util.Objects.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.event.views.CakeIncreaseViewsEvent;
import com.cakk.domain.mysql.repository.reader.CakeReader;

@RequiredArgsConstructor
@Service
public class ViewsService {

	private final CakeReader cakeReader;

	private final ApplicationEventPublisher publisher;

	@Transactional(readOnly = true)
	public void increaseCakeViews(final Long cakeId) {
		if (isNull(cakeId)) {
			return;
		}

		try {
			final Cake cake = cakeReader.findById(cakeId);
			final CakeIncreaseViewsEvent event = cake.getInCreaseViewsEvent();

			publisher.publishEvent(event);
		} catch (CakkException ignored) {
			// ignored
		}
	}
}
