package com.cakk.core.facade.cake

import com.cakk.domain.mysql.annotation.Reader
import com.cakk.domain.mysql.entity.user.User
import java.util.*
import java.util.function.Supplier

class CakeReader {
    private val cakeJpaRepository: CakeJpaRepository? = null
    private val cakeQueryRepository: CakeQueryRepository? = null
    fun findById(cakeId: Long?): Cake {
        return cakeJpaRepository.findById(cakeId).orElseThrow<CakkException>(Supplier<CakkException> { CakkException(ReturnCode.NOT_EXIST_CAKE) })
    }

    fun findByIdWithHeart(cakeId: Long?): Cake {
        val cake: Cake = cakeQueryRepository.searchByIdWithHeart(cakeId)
        if (Objects.isNull(cake)) {
            throw CakkException(ReturnCode.NOT_EXIST_CAKE)
        }
        return cake
    }

    fun searchCakeImagesByCursorAndCategory(
            cakeId: Long?,
            category: CakeDesignCategory?,
            pageSize: Int
    ): List<CakeImageResponseParam> {
        return cakeQueryRepository.searchCakeImagesByCursorAndCategory(cakeId, category, pageSize)
    }

    fun searchCakeImagesByCursorAndCakeShopId(
            cakeId: Long?,
            cakeShopId: Long?,
            pageSize: Int
    ): List<CakeImageResponseParam> {
        return cakeQueryRepository.searchCakeImagesByCursorAndCakeShopId(cakeId, cakeShopId, pageSize)
    }

    fun searchCakeImagesByCursorAndSearchKeyword(param: CakeSearchParam): List<CakeImageResponseParam> {
        return cakeQueryRepository.searchCakeImagesByCursorAndSearchKeyword(
                param.cakeId,
                param.keyword,
                param.location,
                param.pageSize
        )
    }

    fun searchCakeImagesByCakeIds(cakeIds: List<Long?>?): List<CakeImageResponseParam> {
        return cakeQueryRepository.searchCakeImagesByCakeIds(cakeIds)
    }

    fun findWithCakeTagsAndCakeCategories(cakeId: Long?, owner: User?): Cake {
        return cakeQueryRepository.searchWithCakeTagsAndCakeCategories(cakeId, owner)
                .orElseThrow<CakkException>(Supplier<CakkException> { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) })
    }

    fun searchCakeDetailById(cakeId: Long?): CakeDetailParam {
        val param: CakeDetailParam = cakeQueryRepository.searchCakeDetailById(cakeId)
        if (Objects.isNull(param)) {
            throw CakkException(ReturnCode.NOT_EXIST_CAKE)
        }
        return param
    }
}
