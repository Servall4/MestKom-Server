package com.mestKom.sources

import com.mestKom.data.user.User

interface UserDataSource {
    suspend fun allUsers() : List<User>
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertUser(user: User)
    suspend fun editUser(email:String, password:String, username:String): Boolean
    suspend fun deleteUser(username: String): Boolean
    suspend fun getUserByEmail(email: String) : User?
    suspend fun getUserById(id: String): User?
}