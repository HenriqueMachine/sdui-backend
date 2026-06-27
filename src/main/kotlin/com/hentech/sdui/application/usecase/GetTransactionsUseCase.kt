package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.TransactionRepository
import com.hentech.sdui.domain.entity.Transaction

class GetTransactionsUseCase(private val transactionRepository: TransactionRepository) {
    fun execute(userId: String): List<Transaction> =
        transactionRepository.findByUserId(userId)
}
