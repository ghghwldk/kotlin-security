package com.kotlinsecurity.template.controller.dto

import com.kotlinsecurity.template.entity.Member

data class MemberResponseDto(
        val email: String
) {
    companion object {
        fun of(member: Member): MemberResponseDto? {
            return member.email?.let { MemberResponseDto(it) }
        }
    }
}
