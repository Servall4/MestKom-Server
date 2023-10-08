package com.mestKom.requests

@kotlinx.serialization.Serializable
data class MessageRequest(
    val idVideo: String,
    val username: String,
    val text: String
)
