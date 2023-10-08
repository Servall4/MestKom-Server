package com.mestKom.data.video

import org.jetbrains.exposed.dao.id.EntityID


data class Video(
    val descriptor: String,
    val latitude: String,
    val longitude: String,
    val path: String,
    val sequelId: String,
    val name: String,
    val idVideo: String
    )
