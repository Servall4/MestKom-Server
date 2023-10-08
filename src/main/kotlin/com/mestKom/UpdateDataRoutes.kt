package com.mestKom

import com.mestKom.requests.RegRequest
import com.mestKom.requests.UpdateDataRequest
import com.mestKom.responses.UserResponse
import com.mestKom.sources.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateData(userDataSource: UserDataSource){
    post("user"){
        val request = kotlin.runCatching { call.receiveNullable<UpdateDataRequest>() }.getOrNull() ?: kotlin.run{
            call.respond(HttpStatusCode.Conflict, "Not json")
            return@post
        }
        val user = userDataSource.getUserById(request.id)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, message = "User not found")
            return@post
        }

        call.respond(HttpStatusCode.OK, message = UserResponse(user!!.username , user!!.email, user!!.sequelId, user.dateRegistration.toString()))
    }
}

