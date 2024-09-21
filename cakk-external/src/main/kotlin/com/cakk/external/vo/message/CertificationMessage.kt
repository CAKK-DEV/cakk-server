package com.cakk.external.vo.message

data class CertificationMessage(
    val businessRegistrationImageUrl: String,
    val idCardImageUrl: String,
    val emergencyContact: String,
    val message: String,
    val userId: Long,
    val userEmail: String,
    val shopName: String,
    val latitude: Double,
    val longitude: Double
)
