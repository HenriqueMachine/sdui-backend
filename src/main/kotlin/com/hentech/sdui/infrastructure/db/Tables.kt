package com.hentech.sdui.infrastructure.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Users : Table("users") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    override val primaryKey = PrimaryKey(id)
}

object Accounts : Table("accounts") {
    val id = varchar("id", 50)
    val userId = varchar("user_id", 50).references(Users.id)
    val balance = decimal("balance", 15, 2)
    val currency = varchar("currency", 10)
    override val primaryKey = PrimaryKey(id)
}

object Transactions : Table("transactions") {
    val id = varchar("id", 50)
    val userId = varchar("user_id", 50).references(Users.id)
    val contactId = varchar("contact_id", 50).nullable()
    val description = varchar("description", 255)
    val amount = decimal("amount", 15, 2)
    val date = date("date")
    val type = varchar("type", 10)
    override val primaryKey = PrimaryKey(id)
}

object Contacts : Table("contacts") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val bankCode = varchar("bank_code", 10)
    val accountNumber = varchar("account_number", 30)
    override val primaryKey = PrimaryKey(id)
}
