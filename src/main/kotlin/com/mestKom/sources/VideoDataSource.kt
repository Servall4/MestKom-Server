package com.mestKom.sources

import com.mestKom.data.video.Video


interface VideoDataSource {

    suspend fun allVideo() : List<Video>

    suspend fun getVideoById(idVideo: String): Video?

    suspend fun insertVideo(video: Video)

    suspend fun editVideo(nameFile: String, descriptor: String): Boolean

    suspend fun deleteVideo(idVideo: String): Boolean

    suspend fun getAllVideoOfUser(idUser: String) : List<Video>

    suspend fun getVideoByUserId(idUser: String) : Video?
}