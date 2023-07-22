package com.kotlinsecurity.template.repository

import com.kotlinsecurity.template.entity.Member
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Optional<Member>
    fun existsByEmail(email: String): Boolean
}
