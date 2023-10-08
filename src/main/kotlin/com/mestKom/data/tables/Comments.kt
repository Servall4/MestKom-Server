package com.mestKom.data.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Comments : IntIdTable(){
    val idVideo = varchar("idVideo", 100)
    val username = varchar("username", 12)
    val text = text("text")
}