package com.mestKom.responses
@kotlinx.serialization.Serializable
data class CommentResponse(
    val username: String,
    val text: String
)
