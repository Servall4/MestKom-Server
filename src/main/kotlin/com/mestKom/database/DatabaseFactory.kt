package com.mestKom.database

import com.mestKom.data.tables.Comments
import com.mestKom.data.tables.Users
import com.mestKom.data.tables.Videos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/mestKom",
            driver = "org.postgresql.Driver",
            user = "test",
            password = "test"
        )

        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Videos)
            SchemaUtils.create(Comments)
        }
    }



    suspend fun <T> dbQuery(block: suspend() -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}