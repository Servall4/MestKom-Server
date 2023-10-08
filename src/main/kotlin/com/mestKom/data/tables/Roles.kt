package com.mestKom.data.tables

import org.jetbrains.exposed.dao.id.IntIdTable

class Roles : IntIdTable() {
    var name = varchar("Name", 45)
}