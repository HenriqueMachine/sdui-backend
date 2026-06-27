package com.hentech.sdui.domain.entity

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: String,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val type: TransactionType
)
