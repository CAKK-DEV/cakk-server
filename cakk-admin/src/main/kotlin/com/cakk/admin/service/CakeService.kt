package com.cakk.admin.service

import com.cakk.admin.dto.param.CakeCreateByAdminParam
import com.cakk.admin.dto.param.CakeUpdateByAdminParam
import com.cakk.domain.mysql.repository.reader.CakeReader
import com.cakk.domain.mysql.repository.reader.CakeShopReader
import com.cakk.domain.mysql.repository.reader.TagReader
import com.cakk.domain.mysql.repository.writer.CakeWriter
import com.cakk.domain.mysql.repository.writer.TagWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CakeService(
    private val cakeShopReader: CakeShopReader,
    private val cakeReader: CakeReader,
    private val cakeWriter: CakeWriter,
    private val tagReader: TagReader,
    private val tagWriter: TagWriter
) {

    @Transactional
    fun createCake(dto: CakeCreateByAdminParam) {
        val cakeShop = cakeShopReader.findById(dto.cakeShopId)
        val cake = dto.cake
        val tags = dto.tagNames
            .map {
                tagReader.findByTagName(it).orElseGet { tagWriter.saveTag(it) }
            }.toMutableList()

        cake.registerTags(tags)
        cake.registerCategories(dto.cakeCategories)
        cakeShop.registerCake(cake)
    }

    @Transactional
    fun updateCake(dto: CakeUpdateByAdminParam) {
        val cake = cakeReader.findById(dto.cakeId)
        val tags = dto.tagNames
            .map {
                tagReader.findByTagName(it).orElseGet { tagWriter.saveTag(it) }
            }.toMutableList()

        cake.updateCakeImageUrl(dto.cakeImageUrl)
        cake.updateCakeCategories(dto.cakeCategories)
        cake.updateCakeTags(tags)
    }

    @Transactional
    fun deleteCake(cakeId: Long) {
        val cake = cakeReader.findById(cakeId)

        cake.removeCakeCategories()
        cake.removeCakeTags()
        cakeWriter.deleteCake(cake)
    }
}
