package com.cakk.admin.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

import com.cakk.infrastructure.persistence.entity.user.UserEntity

class OAuthUserDetails(
	private val userEntity: UserEntity,
	private val attribute: Map<String, Any>
) : UserDetails, OidcUser, OAuth2User {

    constructor(userEntity: UserEntity) : this(userEntity, mapOf<String, Any>("id" to userEntity.id))

	fun getUser(): UserEntity = userEntity

    override fun getName(): String {
        return userEntity.id.toString()
    }

    override fun getAttributes(): Map<String, Any> {
        return attribute
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(userEntity.role.securityRole))
    }

    override fun getPassword(): String {
        return "password"
    }

    override fun getUsername(): String {
        return userEntity.id.toString()
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
