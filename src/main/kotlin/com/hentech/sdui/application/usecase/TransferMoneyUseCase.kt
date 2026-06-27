package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.AccountRepository
import com.hentech.sdui.application.port.ContactRepository
import com.hentech.sdui.application.port.TransactionRepository
import com.hentech.sdui.domain.entity.Transaction
import com.hentech.sdui.domain.exception.InsufficientBalanceException
import java.math.BigDecimal

data class TransferInput(val userId: String, val contactId: String, val amount: BigDecimal)
data class TransferOutput(val transaction: Transaction, val newBalance: BigDecimal)

class TransferMoneyUseCase(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val contactRepository: ContactRepository
) {
    fun execute(input: TransferInput): TransferOutput {
        val account = accountRepository.findByUserId(input.userId)
            ?: error("Account not found for user: ${input.userId}")
        val contact = contactRepository.findById(input.contactId)
            ?: error("Contact not found: ${input.contactId}")

        if (account.balance < input.amount) {
            throw InsufficientBalanceException(account.balance, input.amount)
        }

        accountRepository.debit(account.id, input.amount)
        val transaction = transactionRepository.save(
            userId = input.userId,
            contactId = input.contactId,
            amount = input.amount,
            description = "Transferência para ${contact.name}"
        )

        return TransferOutput(transaction = transaction, newBalance = account.balance - input.amount)
    }
}
