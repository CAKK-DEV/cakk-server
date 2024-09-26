package com.cakk.api.filter

import java.io.IOException

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import com.fasterxml.jackson.databind.ObjectMapper

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.response.ApiResponse

@Component
class JwtExceptionFilter : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: CakkException) {
            setErrorResponse(exception.getReturnCode(), response)
        }
    }

    private fun setErrorResponse(returnCode: ReturnCode, response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json; charset=UTF-8"

        val result = ApiResponse.fail<String>(returnCode)

        try {
            response.writer.write(toJson(result))
        } catch (e: IOException) {
            // ignored
        }
    }

    private fun toJson(data: Any): String {
        return ObjectMapper().writeValueAsString(data)
    }
}
