package com.cakk.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.admin.dto.param.CakeCreateByAdminParam
import com.cakk.admin.dto.param.CakeUpdateByAdminParam
import com.cakk.domain.mysql.facade.cake.CakeManagerFacade
import com.cakk.domain.mysql.facade.tag.TagManagerFacade
import com.cakk.domain.mysql.repository.reader.CakeReader
import com.cakk.domain.mysql.repository.reader.CakeShopReader

@Service
class CakeService(
	private val cakeShopReader: CakeShopReader,
	private val cakeReader: CakeReader,
	private val tagManagerFacade: TagManagerFacade,
	private val cakeManagerFacade: CakeManagerFacade
) {

	@Transactional
	fun createCake(dto: CakeCreateByAdminParam) {
		val cakeShop = cakeShopReader.findById(dto.cakeShopId)
		val cake = dto.cake
		val tags = dto.tagNames.map { tagManagerFacade.saveIfNew(it) }.toMutableList()

		cakeManagerFacade.create(cakeShop, cake, tags, dto.cakeCategories)
	}

	@Transactional
	fun updateCake(dto: CakeUpdateByAdminParam) {
		val cake = cakeReader.findById(dto.cakeId)
		val tags = dto.tagNames.map { tagManagerFacade.saveIfNew(it) }.toMutableList()

		cakeManagerFacade.update(cake, dto.cakeImageUrl, tags, dto.cakeCategories)
	}

	@Transactional
	fun deleteCake(cakeId: Long) {
		val cake = cakeReader.findById(cakeId)

		cakeManagerFacade.delete(cake)
	}
}
