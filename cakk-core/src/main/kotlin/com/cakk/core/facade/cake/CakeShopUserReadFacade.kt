package com.cakk.core.facade.cake



class CakeShopHeartReader {
    private val cakeShopHeartQueryRepository: CakeShopHeartQueryRepository? = null
    private val cakeShopHeartJpaRepository: CakeShopHeartJpaRepository? = null
    fun searchAllByCursorAndHeart(
            cakeShopHeartId: Long?,
            userId: Long?,
            pageSize: Int
    ): List<HeartCakeShopResponseParam> {
        val cakeShopHeartIds: List<Long> = cakeShopHeartQueryRepository.searchIdsByCursorAndHeart(cakeShopHeartId, userId, pageSize)
        return cakeShopHeartQueryRepository.searchAllByCursorAndHeart(cakeShopHeartIds)
    }
}
