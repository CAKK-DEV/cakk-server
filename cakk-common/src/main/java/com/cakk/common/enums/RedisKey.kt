package com.cakk.common.enums

import com.cakk.common.exception.CakkException

enum class RedisKey(
    val value: String
) {
    REFRESH_TOKEN("REFRESH_TOKEN::"),
    EMAIL_VERIFICATION("EMAIL::"),
    SEARCH_KEYWORD("SEARCH::keyword"),
    VIEWS_CAKE("VIEWS::cake"),
    VIEWS_CAKE_SHOP("VIEWS::cake-shop"),
    LOCK_CAKE_HEART("LOCK::cake-heart"),
    LOCK_SHOP_HEART("LOCK::shop-heart"),
    LOCK_SHOP_LIKE("LOCK::shop-like");

    companion object {
        @JvmStatic
        fun getLockByMethodName(method: String): RedisKey {
            return when (method) {
                "heartCake" -> LOCK_CAKE_HEART
                "heartCakeShop" -> LOCK_SHOP_HEART
                "likeCakeShop" -> LOCK_SHOP_LIKE
                else -> throw CakkException(ReturnCode.INTERNAL_SERVER_ERROR)
            }
        }
    }
}
