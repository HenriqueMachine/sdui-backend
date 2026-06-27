package com.hentech.sdui.adapter.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ScreenDto(
    val screenName: String,
    val schemaVersion: String = "v1",
    val backBehavior: String? = null,
    val components: List<ComponentDto>
)

@Serializable
data class ComponentDto(
    val type: String,
    val props: JsonObject = JsonObject(emptyMap()),
    val children: List<ComponentDto> = emptyList(),
    val actions: List<ActionDto> = emptyList(),
    val critical: Boolean = false,
    val fallback: ComponentDto? = null
)

@Serializable
data class ActionDto(
    val type: String,
    val to: String? = null,
    val endpoint: String? = null,
    val method: String? = null,
    val fieldKeys: List<String>? = null,
    val toggleKey: String? = null,
    val message: String? = null
)
