package com.kotlinsecurity.template.service

import com.kotlinsecurity.template.entity.Member
import com.kotlinsecurity.template.repository.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val member: Member = memberRepository.findByEmail(username)
                .orElseThrow { UsernameNotFoundException("$username -> 데이터베이스에서 찾을 수 없습니다.") }

        return createUserDetails(member)
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private fun createUserDetails(member: Member): UserDetails {
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority(member.authority.toString())

        return User(
                member.id.toString(),
                member.password,
                setOf(grantedAuthority)
        )
    }
}