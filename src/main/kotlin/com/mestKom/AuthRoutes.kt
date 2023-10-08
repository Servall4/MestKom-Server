package com.mestKom

import com.mestKom.data.tables.Users.defaultExpression
import com.mestKom.data.user.User
import com.mestKom.requests.AuthRequest
import com.mestKom.requests.RegRequest
import com.mestKom.responses.AuthResponse
import com.mestKom.security.hashing.HashingService
import com.mestKom.security.hashing.SaltedHash
import com.mestKom.security.token.TokenClaim
import com.mestKom.security.token.TokenConfig
import com.mestKom.security.token.TokenService
import com.mestKom.sources.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import java.io.File
import java.time.LocalDateTime
import java.util.*


fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
){
    post("signup") {
        val request = kotlin.runCatching { call.receiveNullable<RegRequest>() }.getOrNull() ?: kotlin.run{
            call.respond(HttpStatusCode.Conflict, "Not json")
            return@post
        }
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        val checkUserByUsername = userDataSource.getUserByUsername(request.username)
        val checkUserByEmail = userDataSource.getUserByEmail(request.email)

        //Провекра на свободный email
        if(checkUserByEmail != null){
            call.respond(HttpStatusCode.Conflict, "Email is busy")
            return@post
        }
        if(request.username.length > 12){
            call.respond(HttpStatusCode.Conflict, "Username length is less than 12 characters")
            return@post
        }
        //Проверка на существованния пользователя
        if(checkUserByUsername != null){
            call.respond(HttpStatusCode.Conflict, "User is busy")
            return@post
        }
        //Проверка на длинну пароля
        if(isPwTooShort){
            call.respond(HttpStatusCode.Conflict, "Password length is less than 8 characters")
            return@post
        }
        //Проверка на пусты поля
        if(areFieldsBlank){
            call.respond(HttpStatusCode.Conflict, "Password or email is empty")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val id = UUID.randomUUID().toString()
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            email = request.email,
            sequelId = id,
            dateRegistration = LocalDateTime.now()
        )

                    val wasAcknowledged = userDataSource.insertUser(user)
                    if (wasAcknowledged == null) {
                        call.respond(HttpStatusCode.Conflict)
                        return@post

    }
        val f = File("D:\\video", id)
        f.mkdir()
        call.respond(HttpStatusCode.OK, message = AuthResponse(id, request.username, email = request.email, id))
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post("signin") {
        val request = kotlin.runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if (user == null){
            call.respond(HttpStatusCode.Conflict, "User not found")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "invalid password")
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "UserId",
                value = user.sequelId
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token,
                id = user.sequelId,
                username = user.username,
                email = user.email
            )
        )
    }
}

fun Route.authenticate(){
    authenticate {
        get("user"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
           // call.respond(HttpStatusCode.OK)
        }
    }
}