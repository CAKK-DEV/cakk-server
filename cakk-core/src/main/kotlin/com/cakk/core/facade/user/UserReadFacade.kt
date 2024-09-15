package com.cakk.core.facade.user

import com.cakk.domain.mysql.annotation.Reader
import com.cakk.domain.mysql.entity.user.User
import java.util.*
import java.util.function.Supplier



class UserReader {
    private val userJpaRepository: UserJpaRepository? = null
    private val userQueryRepository: UserQueryRepository? = null
    fun findByUserId(userId: Long?): User {
        return userJpaRepository.findById(userId).orElseThrow<CakkException>(Supplier<CakkException> { CakkException(ReturnCode.NOT_EXIST_USER) })
    }

    fun findByProviderId(providerId: String?): User {
        return userJpaRepository.findByProviderId(providerId).orElseThrow<CakkException>(Supplier<CakkException> { CakkException(ReturnCode.NOT_EXIST_USER) })
    }

    fun findByIdWithAll(userId: Long?): User {
        val user: User = userQueryRepository.searchByIdWithAll(userId)
        if (Objects.isNull(user)) {
            throw CakkException(ReturnCode.NOT_EXIST_USER)
        }
        return user
    }

    fun findAll(): List<User> {
        return userJpaRepository.findAll()
    }
}
