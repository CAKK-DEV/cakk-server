package com.cakk.api.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

import com.cakk.domain.mysql.entity.user.User

class OAuthUserDetails(
	private val user: User,
	private val attribute: Map<String, Any>
) : UserDetails, OidcUser, OAuth2User {

    constructor(user: User) : this(user, mapOf<String, Any>("id" to user.id))

	fun getUser(): User = user

    override fun getName(): String {
        return user.id.toString()
    }

    override fun getAttributes(): Map<String, Any> {
        return attribute
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(user.role.securityRole))
    }

    override fun getPassword(): String {
        return "password"
    }

    override fun getUsername(): String {
        return user.id.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getClaims(): Map<String, Any>? {
        return null
    }

    override fun getUserInfo(): OidcUserInfo? {
        return null
    }

    override fun getIdToken(): OidcIdToken? {
        return null
    }
}
