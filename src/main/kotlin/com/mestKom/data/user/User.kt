package com.mestKom.data.user

import java.time.LocalDateTime

data class User(
    val username: String,
    val password: String,
    val email: String,
    val salt: String,
    val sequelId: String,
    val dateRegistration: LocalDateTime
)

