package com.cakk.common.utils

import java.util.*

fun decodeBase64(string: String): ByteArray {
	return Base64.getUrlDecoder().decode(string)
}
