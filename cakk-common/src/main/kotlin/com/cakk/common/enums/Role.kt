package com.cakk.common.enums

enum class Role {

    ADMIN,
	USER;

    val securityRole: String
        get() = "ROLE_$this"
}
