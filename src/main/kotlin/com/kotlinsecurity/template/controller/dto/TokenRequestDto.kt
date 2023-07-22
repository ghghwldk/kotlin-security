package com.kotlinsecurity.template.controller.dto

data class TokenRequestDto(
        val accessToken: String? = null,
        val refreshToken: String? = null
)
