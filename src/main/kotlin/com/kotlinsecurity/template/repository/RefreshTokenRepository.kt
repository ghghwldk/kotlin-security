package com.kotlinsecurity.template.repository

import com.kotlinsecurity.template.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByKey(key: String): Optional<RefreshToken>
}