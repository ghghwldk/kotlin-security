package com.kotlinsecurity.template.controller

import com.kotlinsecurity.template.controller.dto.MemberResponseDto
import com.kotlinsecurity.template.service.MemberService
import com.kotlinsecurity.template.util.SecurityUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @GetMapping("/me")
    fun findMemberInfoById(): ResponseEntity<MemberResponseDto> {
        val memberId = SecurityUtil.getCurrentMemberId()
        val responseDto = memberService.findMemberInfoById(memberId)
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/{email}")
    fun findMemberInfoByEmail(@PathVariable email: String): ResponseEntity<MemberResponseDto> {
        val responseDto = memberService.findMemberInfoByEmail(email)
        return ResponseEntity.ok(responseDto)
    }
}
