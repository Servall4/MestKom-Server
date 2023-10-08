package com.mestKom.sources

import com.mestKom.data.Comment.Comment
import com.mestKom.data.tables.Comments
import com.mestKom.data.tables.Users
import com.mestKom.data.tables.Videos
import com.mestKom.data.user.User
import com.mestKom.data.video.Video
import com.mestKom.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseDataSource : UserDataSource, VideoDataSource, CommentDataSource {

    private fun result(row: ResultRow) = User(
        username = row[Users.username],
        password = row[Users.password],
        email = row[Users.email],
        salt = row[Users.salt],
        sequelId = row[Users.sequelId],
        dateRegistration = row[Users.dateRegistration]
    )

    private fun resultVideo(row: ResultRow) = Video(
        descriptor = row[Videos.descriptor],
        latitude = row[Videos.latitude],
        longitude = row[Videos.longitude],
        path = row[Videos.path],
        sequelId = row[Videos.sequelId],
        name = row[Videos.name],
        idVideo = row[Videos.idVideo]
    )
    private fun resultComment(row: ResultRow) = Comment(
        idVideo = row[Comments.idVideo],
        username = row[Comments.username],
        text = row[Comments.text]
    )


    override suspend fun allUsers(): List<User> = dbQuery{
        Users.selectAll().map(::result)
    }

    override suspend fun getCommentById(videoId: String): List<Comment?> = dbQuery {
        Comments
            .select{Comments.idVideo eq videoId}
            .map(::resultComment)
    }

    override suspend fun insertComment(comment: Comment): Unit = dbQuery {
        Comments.insertAndGetId{
            it[Comments.idVideo] = comment.idVideo
            it[Comments.username] = comment.username
            it[Comments.text]    = comment.text
        }
    }

    override suspend fun editComment(text: String, idVideo: String, username: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUsername(username: String): User? = dbQuery{
        Users
            .select{Users.username eq username}
            .map(::result)
            .singleOrNull()
    }

    override suspend fun insertUser(user: User): Unit = dbQuery{
        //val insertStatement =
            Users.insertAndGetId{
            it[Users.username] = user.username
            it[Users.password] = user.password
            it[Users.email]    = user.email
            it[Users.salt]     = user.salt
            it[Users.sequelId]       = user.sequelId
        }
        //insertStatement.resultedValues?.singleOrNull()?.let (::result)
    }

    override suspend fun editUser(email: String, password: String, username: String): Boolean = dbQuery {
        Users.update({Users.username eq username}) {

            it[Users.email]    = email
            it[Users.password] = password
            it[Users.username] = username
        } > 0
    }

    override suspend fun deleteUser(username: String): Boolean = dbQuery {
        Users.deleteWhere { Users.username eq username } > 0
    }

    override suspend fun getUserByEmail(email: String): User? = dbQuery{
        Users
            .select{Users.email eq email}
            .map(::result)
            .singleOrNull()
    }

    override suspend fun getUserById(id: String): User? = dbQuery{
        Users
            .select{Users.sequelId eq id}
            .map(::result)
            .singleOrNull()
    }

    override suspend fun allVideo(): List<Video> = dbQuery{
        Videos.selectAll().map(::resultVideo)
    }

    override suspend fun getVideoById(idVideo: String): Video? = dbQuery {
        Videos
            .select{Videos.idVideo eq idVideo}
            .map(::resultVideo)
            .singleOrNull()
    }

    override suspend fun insertVideo(video: Video) : Unit = dbQuery{
        Videos.insertAndGetId{
            it[Videos.descriptor] = video.descriptor
            it[Videos.latitude]   = video.latitude
            it[Videos.longitude]  = video.longitude
            it[Videos.path]       = video.path
            it[Videos.sequelId]   = video.sequelId
            it[Videos.name]       = video.name
            it[Videos.idVideo]    = video.idVideo
        }
    }

    override suspend fun editVideo(nameFile: String, descriptor: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVideo(idVideo: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAllVideoOfUser(idUser: String): List<Video> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoByUserId(idUser: String): Video? = dbQuery{
        Videos
            .select{Videos.sequelId eq idUser}
            .map(::resultVideo)
            .singleOrNull()
    }

}