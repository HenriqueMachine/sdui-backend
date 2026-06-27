package com.hentech.sdui.adapter.persistence

import com.hentech.sdui.application.port.ContactRepository
import com.hentech.sdui.domain.entity.Contact
import com.hentech.sdui.infrastructure.db.Contacts
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedContactRepository : ContactRepository {
    override fun findAll(): List<Contact> = transaction {
        Contacts.selectAll().map {
            Contact(it[Contacts.id], it[Contacts.name], it[Contacts.bankCode], it[Contacts.accountNumber])
        }
    }

    override fun findById(id: String): Contact? = transaction {
        Contacts.selectAll().where { Contacts.id eq id }
            .map { Contact(it[Contacts.id], it[Contacts.name], it[Contacts.bankCode], it[Contacts.accountNumber]) }
            .singleOrNull()
    }
}
