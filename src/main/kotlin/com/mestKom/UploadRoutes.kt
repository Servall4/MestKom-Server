package com.mestKom

import com.google.gson.Gson

import com.mestKom.data.video.Video
import com.mestKom.data.video.VideoJSON
import com.mestKom.responses.UpdateResponse
import com.mestKom.sources.UserDataSource
import com.mestKom.sources.VideoDataSource
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun Route.uploadVideo(videoDataSource: VideoDataSource){
    var fileName = ""
    var fileDescriptor = ""
    var fileBytes = ByteArray(0)

    post("/uploadVideo") {
        val multipart = call.receiveMultipart()

        multipart.forEachPart {part ->
            when(part){
                is PartData.FormItem -> {
                    fileDescriptor = """${part.value.trimIndent()}"""
                }
                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    fileBytes = part.streamProvider().readBytes()
                }
                else -> {}
            }
            part.dispose()
        }
        println(fileDescriptor)

        val videoJson = Gson().fromJson(/* json = */ fileDescriptor, /* classOfT = */ VideoJSON::class.java)

        println("fileName: ${videoJson.name}")
        val id = UUID.randomUUID().toString()
        println("id: ${id}")
        val video = Video(
            descriptor = videoJson.description,
            latitude = videoJson.latitude,
            longitude = videoJson.longitude,
            path = "/videos/${videoJson.id}/${videoJson.name}.mp4",
            sequelId = videoJson.id,
            name = videoJson.name,
            idVideo = id
        )

        videoDataSource.insertVideo(video)
        Files.createDirectories(Paths.get("/videos/${videoJson.id}"))
        File("/videos/${videoJson.id}/${videoJson.name}.mp4").writeBytes(fileBytes)
        call.respond(HttpStatusCode.OK)
    }

}

fun Route.lastChange(videoDataSource: VideoDataSource, userDataSource: UserDataSource){
    get ("update"){
        val videos = videoDataSource.allVideo()
        val listUpdate : Vector<UpdateResponse> = Vector<UpdateResponse>()
        for(video in videos){
            val username = userDataSource.getUserById(video.sequelId)
            listUpdate.add(UpdateResponse(video.idVideo, video.descriptor, video.latitude, video.longitude, video.name, username!!.username))
        }
        call.respond(HttpStatusCode.OK, message = listUpdate)
    }
}

fun Route.getVideo(videoDataSource: VideoDataSource){
    get("video/{id}") {
        val video = videoDataSource.getVideoById(call.parameters["id"]!!)!!
        call.respondFile(File(video.path))
    }
}
