package com.mestKom.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val id: String,
    val username: String,
    val email: String,
    val token: String
)
