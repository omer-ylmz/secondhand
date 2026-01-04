package com.omer.secondhand.user.dto

data class CreateUserRequestkt(
    val mail: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
) {
}