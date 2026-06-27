package com.hentech.sdui.adapter.persistence

import com.hentech.sdui.application.port.AccountRepository
import com.hentech.sdui.domain.entity.Account
import com.hentech.sdui.infrastructure.db.Accounts
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal

class ExposedAccountRepository : AccountRepository {
    override fun findByUserId(userId: String): Account? = transaction {
        Accounts.selectAll().where { Accounts.userId eq userId }
            .map { Account(it[Accounts.id], it[Accounts.userId], it[Accounts.balance], it[Accounts.currency]) }
            .singleOrNull()
    }

    override fun debit(accountId: String, amount: BigDecimal): Unit = transaction {
        val current = Accounts.selectAll().where { Accounts.id eq accountId }
            .map { it[Accounts.balance] }.single()
        Accounts.update({ Accounts.id eq accountId }) {
            it[balance] = current - amount
        }
    }
}
