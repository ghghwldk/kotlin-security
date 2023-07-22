package com.kotlinsecurity.template.controller.dto

data class TokenDto(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String,
        val accessTokenExpiresIn: Long
)
