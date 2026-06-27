package com.hentech.sdui.adapter.screen

import com.hentech.sdui.adapter.dto.ActionDto
import com.hentech.sdui.adapter.dto.ComponentDto
import com.hentech.sdui.adapter.dto.ScreenDto
import com.hentech.sdui.domain.entity.Contact
import com.hentech.sdui.domain.entity.Transaction
import kotlinx.serialization.json.*
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

class TransferSuccessScreenBuilder {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun build(transaction: Transaction, newBalance: BigDecimal, contact: Contact): ScreenDto = ScreenDto(
        screenName = "transfer_success",
        backBehavior = "DEFAULT",
        components = listOf(
            ComponentDto(
                type = "success_header",
                props = buildJsonObject {
                    put("title", "Transferência realizada!")
                    put("subtitle", "Seu dinheiro está a caminho")
                    put("icon", "check_circle")
                }
            ),
            ComponentDto(
                type = "info_row",
                props = buildJsonObject {
                    put("label", "Para")
                    put("value", contact.name)
                }
            ),
            ComponentDto(
                type = "info_row",
                props = buildJsonObject {
                    put("label", "Valor")
                    put("value", "R$ ${transaction.amount.toPlainString()}")
                }
            ),
            ComponentDto(
                type = "info_row",
                props = buildJsonObject {
                    put("label", "Data")
                    put("value", transaction.date.format(dateFormatter))
                }
            ),
            ComponentDto(
                type = "info_row",
                props = buildJsonObject {
                    put("label", "Novo saldo")
                    put("value", "R$ ${newBalance.toPlainString()}")
                }
            ),
            ComponentDto(
                type = "primary_button",
                props = buildJsonObject { put("label", "Voltar ao início") },
                actions = listOf(ActionDto(type = "NAVIGATE", to = "home")),
                critical = true
            )
        )
    )
}
