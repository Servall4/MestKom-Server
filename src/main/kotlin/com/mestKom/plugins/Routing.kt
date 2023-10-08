package com.mestKom.plugins

import com.mestKom.*
import com.mestKom.security.hashing.HashingService
import com.mestKom.security.token.TokenConfig
import com.mestKom.security.token.TokenService
import com.mestKom.sources.CommentDataSource
import com.mestKom.sources.UserDataSource
import com.mestKom.sources.VideoDataSource
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    videoDataSource: VideoDataSource,
    commentDataSource : CommentDataSource
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        uploadVideo(videoDataSource)
        authenticate()
        updateData(userDataSource)
        lastChange(videoDataSource, userDataSource)
        getVideo(videoDataSource)
        sendComments(commentDataSource)
        setComment(commentDataSource)
    }
}
