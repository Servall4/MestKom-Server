package com.mestKom.requests

@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
