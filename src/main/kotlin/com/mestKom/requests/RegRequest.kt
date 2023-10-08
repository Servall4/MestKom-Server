package com.mestKom.requests

@kotlinx.serialization.Serializable
data class RegRequest(
    val username: String,
    val password: String,
    val email: String
)
