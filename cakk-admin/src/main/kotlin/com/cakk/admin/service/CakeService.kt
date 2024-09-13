package com.cakk.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.admin.dto.param.CakeCreateByAdminParam
import com.cakk.admin.dto.param.CakeUpdateByAdminParam
import com.cakk.core.facade.cake.CakeManageFacade
import com.cakk.core.facade.tag.TagManageFacade
import com.cakk.domain.mysql.repository.reader.CakeReader
import com.cakk.domain.mysql.repository.reader.CakeShopReader

@Service
class CakeService(
	private val cakeShopReader: CakeShopReader,
	private val cakeReader: CakeReader,
	private val tagManageFacade: TagManageFacade,
	private val cakeManageFacade: CakeManageFacade
) {

	@Transactional
	fun createCake(dto: CakeCreateByAdminParam) {
		val cakeShop = cakeShopReader.findById(dto.cakeShopId)
		val cake = dto.cake
		val tags = dto.tagNames.map { tagManageFacade.create(it) }.toMutableList()

		cakeManageFacade.create(cakeShop, cake, tags, dto.cakeCategories)
	}

	@Transactional
	fun updateCake(dto: CakeUpdateByAdminParam) {
		val cake = cakeReader.findById(dto.cakeId)
		val tags = dto.tagNames.map { tagManageFacade.create(it) }.toMutableList()

		cakeManageFacade.update(cake, dto.cakeImageUrl, tags, dto.cakeCategories)
	}

	@Transactional
	fun deleteCake(cakeId: Long) {
		val cake = cakeReader.findById(cakeId)

		cakeManageFacade.delete(cake)
	}
}
