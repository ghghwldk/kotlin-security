package com.kotlinsecurity.template.service

import com.kotlinsecurity.template.controller.dto.MemberResponseDto
import com.kotlinsecurity.template.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(private val memberRepository: MemberRepository) {

    fun findMemberInfoById(memberId: Long): MemberResponseDto? {
        return memberRepository.findById(memberId)
                .map(MemberResponseDto::of)
                .orElseThrow { RuntimeException("로그인 유저 정보가 없습니다.") }
    }

    fun findMemberInfoByEmail(email: String): MemberResponseDto? {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow { RuntimeException("유저 정보가 없습니다.") }
    }
}
