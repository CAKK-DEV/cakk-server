package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.common.enums.VerificationStatus
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BusinessInformationJpaRepository : JpaRepository<BusinessInformationEntity, Long> {

	@Query(
		"select bi from BusinessInformationEntity as bi join fetch bi.cakeShop "
            + "where bi.cakeShop.id =:cakeShopId")
    fun findBusinessInformationWithCakeShop(@Param("cakeShopId") cakeShopId: Long): BusinessInformationEntity?

	fun findAllByUser(userEntity: UserEntity): List<BusinessInformationEntity>

	fun existsBusinessInformationByUserAndCakeShop_Id(owner: UserEntity, cakeShopId: Long): Boolean

    @Query("select bi from BusinessInformationEntity as bi join fetch bi.cakeShop where bi.user =:user")
    fun findAllWithCakeShopByUser(user: UserEntity): List<BusinessInformationEntity>

    @Query(
		"select bi from BusinessInformationEntity as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.verificationStatus =:verificationStatus")
    fun findAllCakeShopBusinessOwnerCandidates(verificationStatus: VerificationStatus): List<BusinessInformationEntity>

    @Query(
		"select bi from BusinessInformationEntity as bi join fetch bi.user join fetch bi.cakeShop"
            + " where bi.user.id =:userId")
    fun findBusinessInformationByUserId(userId: Long): BusinessInformationEntity?
}

