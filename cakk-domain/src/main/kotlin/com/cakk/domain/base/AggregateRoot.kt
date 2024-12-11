package com.cakk.domain.base

import org.springframework.data.annotation.Transient
import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents

abstract class AggregateRoot<T : Domain<T, TID>, TID>(
	@Transient
	private val domainEvents: MutableList<Any> = mutableListOf()
) : Domain<T, TID>() {

	protected fun registerEvent(event: T) {
		domainEvents.add(event)
	}

	@AfterDomainEventPublication
	protected fun clearEvents() {
		domainEvents.clear()
	}

	@DomainEvents
	protected fun events(): List<Any> {
		return domainEvents
	}
}
