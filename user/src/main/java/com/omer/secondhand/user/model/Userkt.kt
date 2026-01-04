package com.omer.secondhand.user.model

import jakarta.persistence.*

@Entity
data class Userkt(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val mail: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,

) {}