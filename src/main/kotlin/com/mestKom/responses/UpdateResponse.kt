package com.mestKom.responses


@kotlinx.serialization.Serializable
data class UpdateResponse(
    val idVideo: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val nameVideo: String,
    val username: String
)
