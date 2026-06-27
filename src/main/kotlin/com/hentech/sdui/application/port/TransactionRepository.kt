package com.hentech.sdui.application.port

import com.hentech.sdui.domain.entity.Transaction
import java.math.BigDecimal

interface TransactionRepository {
    fun findByUserId(userId: String): List<Transaction>
    fun save(userId: String, contactId: String, amount: BigDecimal, description: String): Transaction
}
