package com.cakk.core.service.user

import java.util.*

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.utils.generateRandomStringOnlyNumber
import com.cakk.core.dto.param.user.GenerateCodeParam
import com.cakk.core.dto.param.user.VerifyEmailParam
import com.cakk.core.mapper.supplyEmailWithVerificationCodeSendEventBy
import com.cakk.domain.redis.repository.EmailVerificationRedisRepository

@Service
class EmailVerificationService(
	private val emailVerificationRedisRepository: EmailVerificationRedisRepository,
	private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun sendEmailForVerification(dto: GenerateCodeParam) {
        val email = dto.email
        val code = generateRandomStringOnlyNumber(6)
        emailVerificationRedisRepository.save(email, code)

        val emailEvent = supplyEmailWithVerificationCodeSendEventBy(email, code)
        applicationEventPublisher.publishEvent(emailEvent)
    }

    fun checkEmailVerificationCode(dto: VerifyEmailParam) {
        val email = dto.email
        val code = dto.code
        val verificationCode = emailVerificationRedisRepository.findCodeByEmail(email)

        if (Objects.isNull(verificationCode) || verificationCode != code) {
            throw CakkException(ReturnCode.WRONG_VERIFICATION_CODE)
        }

        emailVerificationRedisRepository.deleteByEmail(email)
    }
}
