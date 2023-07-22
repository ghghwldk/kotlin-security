package com.kotlinsecurity.template.entity

import javax.persistence.*

@Entity
@Table(name = "member")
data class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_id")
        var id: Long? = null,

        var email: String,

        var password: String? = null,

        @Enumerated
        var authority: Authority? = null
)