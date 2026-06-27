package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.AccountRepository
import com.hentech.sdui.application.port.ContactRepository
import com.hentech.sdui.application.port.TransactionRepository
import com.hentech.sdui.domain.entity.*
import com.hentech.sdui.domain.exception.InsufficientBalanceException
import io.mockk.*
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TransferMoneyUseCaseTest {
    private val accountRepo = mockk<AccountRepository>()
    private val transactionRepo = mockk<TransactionRepository>()
    private val contactRepo = mockk<ContactRepository>()
    private val useCase = TransferMoneyUseCase(accountRepo, transactionRepo, contactRepo)

    private val account = Account("acc_1", "user_1", BigDecimal("1000.00"))
    private val contact = Contact("contact_1", "João", "001", "12345-6")
    private val transaction = Transaction(
        "tx_1", "Transferência para João", BigDecimal("100.00"), LocalDate.now(), TransactionType.DEBIT
    )

    @Test
    fun `transfers money and returns new balance`() {
        every { accountRepo.findByUserId("user_1") } returns account
        every { contactRepo.findById("contact_1") } returns contact
        every { accountRepo.debit("acc_1", BigDecimal("100.00")) } just Runs
        every { transactionRepo.save(any(), any(), any(), any()) } returns transaction

        val result = useCase.execute(TransferInput("user_1", "contact_1", BigDecimal("100.00")))

        assertEquals(BigDecimal("900.00"), result.newBalance)
        assertEquals(transaction, result.transaction)
        verify(exactly = 1) { accountRepo.debit("acc_1", BigDecimal("100.00")) }
    }

    @Test
    fun `throws InsufficientBalanceException when balance is too low`() {
        every { accountRepo.findByUserId("user_1") } returns account.copy(balance = BigDecimal("50.00"))
        every { contactRepo.findById("contact_1") } returns contact

        assertFailsWith<InsufficientBalanceException> {
            useCase.execute(TransferInput("user_1", "contact_1", BigDecimal("100.00")))
        }
        verify(exactly = 0) { accountRepo.debit(any(), any()) }
    }
}
