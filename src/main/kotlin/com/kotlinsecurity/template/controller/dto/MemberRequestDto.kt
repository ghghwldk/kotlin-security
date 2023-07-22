package com.kotlinsecurity.template.controller.dto

import com.kotlinsecurity.template.entity.Authority
import com.kotlinsecurity.template.entity.Member
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder


data class MemberRequestDto(
        var email: String,
        var password: String
) {
    fun toMember(passwordEncoder: PasswordEncoder): Member {
        return Member(
                email = email
                , password = passwordEncoder.encode(password),
                authority = Authority.ROLE_USER
        )
    }

    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(email, password)
    }
}