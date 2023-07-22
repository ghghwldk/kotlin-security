package com.kotlinsecurity.template.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "refresh_token")
data class RefreshToken(
        @Id
        @Column(name = "rt_key")
        val key: String,

        @Column(name = "rt_value")
        var value: String
) {
    fun updateValue(token: String): RefreshToken {
        this.value = token
        return this
    }
}