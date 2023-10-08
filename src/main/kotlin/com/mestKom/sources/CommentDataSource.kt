package com.mestKom.sources

import com.mestKom.data.Comment.Comment
import com.mestKom.data.user.User

interface CommentDataSource {
    suspend fun allUsers() : List<User>
    suspend fun getCommentById(videoId: String): List<Comment?>
    suspend fun insertComment(comment: Comment)
    suspend fun editComment(text:String, idVideo:String, username:String): Boolean
    suspend fun deleteComment(username: String): Boolean
}