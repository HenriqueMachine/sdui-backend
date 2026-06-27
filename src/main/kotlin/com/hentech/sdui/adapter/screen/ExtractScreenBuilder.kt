package com.hentech.sdui.adapter.screen

import com.hentech.sdui.adapter.dto.ComponentDto
import com.hentech.sdui.adapter.dto.ScreenDto
import com.hentech.sdui.domain.entity.Transaction
import com.hentech.sdui.domain.entity.TransactionType
import com.hentech.sdui.domain.entity.User
import kotlinx.serialization.json.*
import java.time.format.DateTimeFormatter

class ExtractScreenBuilder {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun build(user: User, transactions: List<Transaction>): ScreenDto = ScreenDto(
        screenName = "extract",
        backBehavior = "DEFAULT",
        components = listOf(
            ComponentDto(
                type = "header",
                props = buildJsonObject {
                    put("title", "Extrato")
                    put("subtitle", user.name)
                },
                critical = true
            ),
            ComponentDto(
                type = "transaction_list",
                props = buildJsonObject {
                    putJsonArray("items") {
                        transactions.forEach { tx ->
                            addJsonObject {
                                put("description", tx.description)
                                put("amount", tx.amount.toPlainString())
                                put("date", tx.date.format(dateFormatter))
                                put("type", if (tx.type == TransactionType.DEBIT) "debit" else "credit")
                            }
                        }
                    }
                },
                critical = true
            )
        )
    )
}
