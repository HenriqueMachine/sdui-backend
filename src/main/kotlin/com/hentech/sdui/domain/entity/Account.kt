package com.hentech.sdui.domain.entity

import java.math.BigDecimal

data class Account(
    val id: String,
    val userId: String,
    val balance: BigDecimal,
    val currency: String = "BRL"
)
