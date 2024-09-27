package com.cakk.domain.mysql.repository.jpa

import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BusinessInformationJpaRepository : JpaRepository<BusinessInformation, Long> {

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop "
            + "where bi.cakeShop.id =:cakeShopId")
    fun findBusinessInformationWithCakeShop(@Param("cakeShopId") cakeShopId: Long): BusinessInformation?

	fun findAllByUser(user: User): List<BusinessInformation>

	fun existsBusinessInformationByUserAndCakeShop_Id(owner: User, cakeShopId: Long): Boolean

    @Query("select bi from BusinessInformation as bi join fetch bi.cakeShop where bi.user =:user")
    fun findAllWithCakeShopByUser(user: User): List<BusinessInformation>

    @Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.verificationStatus =:verificationStatus")
    fun findAllCakeShopBusinessOwnerCandidates(verificationStatus: VerificationStatus): List<BusinessInformation>

    @Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.user.id =:userId")
    fun findBusinessInformationByUserId(userId: Long): BusinessInformation?
}

