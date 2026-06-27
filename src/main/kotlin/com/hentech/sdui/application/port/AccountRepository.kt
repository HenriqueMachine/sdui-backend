package com.hentech.sdui.application.port

import com.hentech.sdui.domain.entity.Account
import java.math.BigDecimal

interface AccountRepository {
    fun findByUserId(userId: String): Account?
    fun debit(accountId: String, amount: BigDecimal)
}
