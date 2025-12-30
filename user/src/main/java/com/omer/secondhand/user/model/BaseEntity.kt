package com.omer.secondhand.user.model;

import java.time.LocalDateTime


data class BaseEntity(
    private val createdDate: LocalDateTime? = null,
    private val updatedDate: LocalDateTime? = null
) {}

