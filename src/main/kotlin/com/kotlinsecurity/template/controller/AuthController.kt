package com.kotlinsecurity.template.controller

import com.kotlinsecurity.template.controller.dto.MemberRequestDto
import com.kotlinsecurity.template.controller.dto.MemberResponseDto
import com.kotlinsecurity.template.controller.dto.TokenDto
import com.kotlinsecurity.template.controller.dto.TokenRequestDto
import com.kotlinsecurity.template.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/signup")
    @ResponseBody
    fun signup(@RequestBody memberRequestDto: MemberRequestDto): ResponseEntity<MemberResponseDto> {
        val responseDto = authService.signup(memberRequestDto)
        return ResponseEntity.ok(responseDto)
    }

    @PostMapping("/signin")
    @ResponseBody
    fun login(@RequestBody memberRequestDto: MemberRequestDto): ResponseEntity<TokenDto> {
        val tokenDto = authService.login(memberRequestDto)
        return ResponseEntity.ok(tokenDto)
    }

    @PostMapping("/reissue")
    @ResponseBody
    fun reissue(@RequestBody tokenRequestDto: TokenRequestDto): ResponseEntity<TokenDto> {
        val tokenDto = authService.reissue(tokenRequestDto)
        return ResponseEntity.ok(tokenDto)
    }
}
