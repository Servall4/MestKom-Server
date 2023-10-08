package com.mestKom


import com.mestKom.data.Comment.Comment
import com.mestKom.requests.MessageRequest
import com.mestKom.responses.CommentResponse
import com.mestKom.responses.MessageResponse
import com.mestKom.sources.CommentDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.Vector

fun Route.sendComments(commentDataSource: CommentDataSource){
    post("/getComments"){
        val request = kotlin.runCatching { call.receiveNullable<MessageResponse>() }.getOrNull() ?: kotlin.run{
            call.respond(HttpStatusCode.Conflict, "Not json")
            return@post
        }
        val comments = commentDataSource.getCommentById(request.idVideo)
        if(comments == null){
            call.respond(HttpStatusCode.Conflict, "Comments not found")
            return@post
        }
        println(comments)
        val vectorComment: Vector<CommentResponse> = Vector<CommentResponse>()

        for(comment in comments){
            vectorComment.add(CommentResponse(comment!!.username, comment!!.text))
        }

        call.respond(HttpStatusCode.OK, message = vectorComment)
    }
}


fun Route.setComment(commentDataSource: CommentDataSource){
    post("/sendComment"){
        val request = kotlin.runCatching { call.receiveNullable<MessageRequest>() }.getOrNull()?: kotlin.run {
            return@post
        }
        println(request)
        commentDataSource.insertComment(Comment(request.idVideo, request.username, request.text))
        call.respond(HttpStatusCode.OK)
    }
}