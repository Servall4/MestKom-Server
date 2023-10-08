package com.mestKom.responses

@kotlinx.serialization.Serializable
data class UserResponse(
    val username: String,
    val email: String,
    val id: String,
    val date: String
)
