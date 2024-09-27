package com.cakk.api.common.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.stereotype.Component

import com.cakk.api.common.annotation.MockCustomUser
import com.cakk.api.vo.OAuthUserDetails
import com.cakk.core.facade.user.UserReadFacade

@Component
class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<MockCustomUser> {

    @Autowired
    private lateinit var userReadFacade: UserReadFacade

    override fun createSecurityContext(annotation: MockCustomUser?): SecurityContext {
        val securityContext = SecurityContextHolder.createEmptyContext()

        val user = userReadFacade.findByUserId(1L)
        val userDetails = OAuthUserDetails(user)
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        securityContext.authentication = authenticationToken
        return securityContext
    }
}
