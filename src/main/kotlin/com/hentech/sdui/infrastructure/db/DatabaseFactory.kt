package com.hentech.sdui.infrastructure.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate

object DatabaseFactory {
    fun init(jdbcUrl: String = "jdbc:sqlite:sdui.db") {
        Database.connect(jdbcUrl, driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Users, Accounts, Transactions, Contacts)
            seed()
        }
    }

    private fun seed() {
        if (Users.selectAll().count() > 0L) return

        Users.insert {
            it[id] = "user_1"
            it[name] = "Maria"
        }

        Accounts.insert {
            it[id] = "acc_1"
            it[userId] = "user_1"
            it[balance] = BigDecimal("5000.00")
            it[currency] = "BRL"
        }

        val contactsData = listOf(
            Triple("contact_1", "João Silva", "001"),
            Triple("contact_2", "Ana Costa", "237"),
            Triple("contact_3", "Pedro Souza", "341")
        )
        contactsData.forEachIndexed { i, (cId, cName, bank) ->
            Contacts.insert {
                it[id] = cId
                it[name] = cName
                it[bankCode] = bank
                it[accountNumber] = "0000${i + 1}-0"
            }
        }

        val txData = listOf(
            Triple("Salário", BigDecimal("3000.00"), "CREDIT"),
            Triple("Transferência para João Silva", BigDecimal("150.00"), "DEBIT"),
            Triple("Transferência para Ana Costa", BigDecimal("75.50"), "DEBIT"),
            Triple("Pix recebido", BigDecimal("200.00"), "CREDIT")
        )
        txData.forEachIndexed { i, (desc, amount, type) ->
            Transactions.insert {
                it[id] = "tx_${i + 1}"
                it[userId] = "user_1"
                it[contactId] = null
                it[description] = desc
                it[Transactions.amount] = amount
                it[date] = LocalDate.now().minusDays(i.toLong())
                it[Transactions.type] = type
            }
        }
    }
}
