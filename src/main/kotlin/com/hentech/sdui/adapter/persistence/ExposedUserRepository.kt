package com.hentech.sdui.adapter.persistence

import com.hentech.sdui.application.port.UserRepository
import com.hentech.sdui.domain.entity.User
import com.hentech.sdui.infrastructure.db.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedUserRepository : UserRepository {
    override fun findById(id: String): User? = transaction {
        Users.selectAll().where { Users.id eq id }
            .map { User(it[Users.id], it[Users.name]) }
            .singleOrNull()
    }
}
