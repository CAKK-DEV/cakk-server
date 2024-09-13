package com.cakk.external.vo.key

data class OidcPublicKey(
    val kid: String,
    val kty: String,
    val alg: String,
    val use: String,
    val n: String,
    val e: String
)
