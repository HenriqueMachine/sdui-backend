package com.hentech.sdui.adapter.screen

import com.hentech.sdui.adapter.dto.ActionDto
import com.hentech.sdui.adapter.dto.ComponentDto
import com.hentech.sdui.adapter.dto.ScreenDto
import com.hentech.sdui.domain.entity.Contact
import kotlinx.serialization.json.*

class TransferScreenBuilder {
    fun build(contacts: List<Contact>): ScreenDto = ScreenDto(
        screenName = "transfer",
        backBehavior = "DEFAULT",
        components = listOf(
            ComponentDto(
                type = "header",
                props = buildJsonObject { put("title", "Transferir") },
                critical = true
            ),
            ComponentDto(
                type = "contact_selector",
                props = buildJsonObject {
                    putJsonArray("contacts") {
                        contacts.forEach { c ->
                            addJsonObject {
                                put("id", c.id)
                                put("name", c.name)
                            }
                        }
                    }
                },
                critical = true
            ),
            ComponentDto(
                type = "amount_input",
                props = buildJsonObject {
                    put("label", "Valor")
                    put("placeholder", "0,00")
                    put("fieldKey", "amount")
                },
                critical = true
            ),
            ComponentDto(
                type = "primary_button",
                props = buildJsonObject { put("label", "Continuar") },
                actions = listOf(ActionDto(type = "NAVIGATE", to = "transfer_confirm")),
                critical = true
            )
        )
    )
}
