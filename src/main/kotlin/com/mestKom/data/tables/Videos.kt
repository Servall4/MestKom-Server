package com.mestKom.data.tables

import com.mestKom.data.tables.Users.defaultExpression
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Videos : IntIdTable() {
    val name = varchar("name", 120)
    val descriptor = varchar("descriptor", 120)
    val latitude = varchar("latitude", 50)
    val longitude = varchar("longitude", 50)
    val path = varchar("path", 120)
    val sequelId = varchar("sequelId", 100)
    val idVideo = varchar("idVideo", 100)
    val dateRegistration = datetime("Date_Registration_Video").defaultExpression(CurrentDateTime)
}