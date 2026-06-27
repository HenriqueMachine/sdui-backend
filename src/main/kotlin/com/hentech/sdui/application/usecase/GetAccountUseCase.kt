package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.AccountRepository
import com.hentech.sdui.domain.entity.Account

class GetAccountUseCase(private val accountRepository: AccountRepository) {
    fun execute(userId: String): Account =
        accountRepository.findByUserId(userId) ?: error("Account not found for user: $userId")
}
