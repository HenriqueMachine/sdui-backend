package com.hentech.sdui.adapter.screen

import com.hentech.sdui.adapter.dto.ActionDto
import com.hentech.sdui.adapter.dto.ComponentDto
import com.hentech.sdui.adapter.dto.ScreenDto
import com.hentech.sdui.domain.entity.Account
import com.hentech.sdui.domain.entity.User
import kotlinx.serialization.json.*

class HomeScreenBuilder {
    fun build(user: User, account: Account): ScreenDto = ScreenDto(
        screenName = "home",
        backBehavior = "DEFAULT",
        components = listOf(
            ComponentDto(
                type = "header",
                props = buildJsonObject {
                    put("title", "Olá, ${user.name}")
                    put("subtitle", "Bem-vindo de volta")
                },
                critical = true
            ),
            ComponentDto(
                type = "balance_card",
                props = buildJsonObject {
                    put("label", "Saldo disponível")
                    put("amount", account.balance.toPlainString())
                    put("currency", account.currency)
                },
                critical = true
            ),
            ComponentDto(
                type = "shortcut_grid",
                props = buildJsonObject {
                    putJsonArray("items") {
                        addJsonObject {
                            put("label", "Transferir")
                            put("icon", "send")
                            putJsonObject("action") {
                                put("type", "NAVIGATE")
                                put("to", "transfer")
                            }
                        }
                        addJsonObject {
                            put("label", "Extrato")
                            put("icon", "list")
                            putJsonObject("action") {
                                put("type", "NAVIGATE")
                                put("to", "extract")
                            }
                        }
                    }
                }
            )
        )
    )
}
