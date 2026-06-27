package com.hentech.sdui.adapter.persistence

import com.hentech.sdui.application.port.TransactionRepository
import com.hentech.sdui.domain.entity.Transaction
import com.hentech.sdui.domain.entity.TransactionType
import com.hentech.sdui.infrastructure.db.Transactions
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class ExposedTransactionRepository : TransactionRepository {
    override fun findByUserId(userId: String): List<Transaction> = transaction {
        Transactions.selectAll().where { Transactions.userId eq userId }
            .orderBy(Transactions.date to SortOrder.DESC)
            .map {
                Transaction(
                    id = it[Transactions.id],
                    description = it[Transactions.description],
                    amount = it[Transactions.amount],
                    date = it[Transactions.date],
                    type = TransactionType.valueOf(it[Transactions.type])
                )
            }
    }

    override fun save(userId: String, contactId: String, amount: BigDecimal, description: String): Transaction {
        val id = UUID.randomUUID().toString()
        val date = LocalDate.now()
        transaction {
            Transactions.insert {
                it[Transactions.id] = id
                it[Transactions.userId] = userId
                it[Transactions.contactId] = contactId
                it[Transactions.description] = description
                it[Transactions.amount] = amount
                it[Transactions.date] = date
                it[Transactions.type] = TransactionType.DEBIT.name
            }
        }
        return Transaction(id, description, amount, date, TransactionType.DEBIT)
    }
}
