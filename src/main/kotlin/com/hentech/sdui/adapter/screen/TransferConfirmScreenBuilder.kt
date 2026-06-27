package com.hentech.sdui.adapter.screen

import com.hentech.sdui.adapter.dto.ActionDto
import com.hentech.sdui.adapter.dto.ComponentDto
import com.hentech.sdui.adapter.dto.ScreenDto
import com.hentech.sdui.domain.entity.Contact
import kotlinx.serialization.json.*
import java.math.BigDecimal

class TransferConfirmScreenBuilder {
    fun build(contact: Contact, amount: BigDecimal, errorMessage: String? = null): ScreenDto {
        val components = mutableListOf(
            ComponentDto(
                type = "header",
                props = buildJsonObject { put("title", "Confirmar transferência") },
                critical = true
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
                    put("value", "R$ ${amount.toPlainString()}")
                }
            )
        )

        if (errorMessage != null) {
            components.add(
                ComponentDto(
                    type = "error_banner",
                    props = buildJsonObject { put("message", errorMessage) }
                )
            )
        }

        components.add(
            ComponentDto(
                type = "primary_button",
                props = buildJsonObject { put("label", "Confirmar") },
                actions = listOf(
                    ActionDto(
                        type = "SUBMIT",
                        endpoint = "/transfers",
                        method = "POST",
                        fieldKeys = listOf("contactId", "amount")
                    )
                ),
                critical = true
            )
        )
        components.add(
            ComponentDto(
                type = "secondary_button",
                props = buildJsonObject { put("label", "Cancelar") },
                actions = listOf(ActionDto(type = "CLOSE"))
            )
        )

        return ScreenDto(
            screenName = "transfer_confirm",
            backBehavior = "CONFIRM_DISCARD",
            components = components
        )
    }
}
