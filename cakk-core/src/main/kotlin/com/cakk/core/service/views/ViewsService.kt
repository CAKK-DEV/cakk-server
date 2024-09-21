package com.cakk.core.service.views

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.common.exception.CakkException
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.mapper.supplyCakeIncreaseViewsEventBy

@Service
class ViewsService(
	private val cakeReadFacade: CakeReadFacade,
	private val publisher: ApplicationEventPublisher
) {

	@Transactional(readOnly = true)
	fun increaseCakeViews(cakeId: Long?) {
		cakeId?.let {
			try {
				val cake = cakeReadFacade.findById(cakeId)
				val event = supplyCakeIncreaseViewsEventBy(cake.id)

				publisher.publishEvent(event)
			} catch (ignored: CakkException) {
				// ignored
			}
		}
	}
}
