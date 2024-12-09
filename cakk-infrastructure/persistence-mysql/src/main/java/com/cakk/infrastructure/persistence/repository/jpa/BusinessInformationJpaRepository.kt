package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.common.enums.VerificationStatus
import com.cakk.infrastructure.persistence.entity.user.BusinessInformation
import com.cakk.infrastructure.persistence.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BusinessInformationJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.user.BusinessInformation, Long> {

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop "
            + "where bi.cakeShop.id =:cakeShopId")
    fun findBusinessInformationWithCakeShop(@Param("cakeShopId") cakeShopId: Long): com.cakk.infrastructure.persistence.entity.user.BusinessInformation?

	fun findAllByUser(user: com.cakk.infrastructure.persistence.entity.user.User): List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation>

	fun existsBusinessInformationByUserAndCakeShop_Id(owner: com.cakk.infrastructure.persistence.entity.user.User, cakeShopId: Long): Boolean

    @Query("select bi from BusinessInformation as bi join fetch bi.cakeShop where bi.user =:user")
    fun findAllWithCakeShopByUser(user: com.cakk.infrastructure.persistence.entity.user.User): List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation>

    @Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.verificationStatus =:verificationStatus")
    fun findAllCakeShopBusinessOwnerCandidates(verificationStatus: VerificationStatus): List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation>

    @Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.user.id =:userId")
    fun findBusinessInformationByUserId(userId: Long): com.cakk.infrastructure.persistence.entity.user.BusinessInformation?
}

