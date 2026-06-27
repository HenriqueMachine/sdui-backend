package com.hentech.sdui.adapter.screen

import com.hentech.sdui.domain.entity.Account
import com.hentech.sdui.domain.entity.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeScreenBuilderTest {
    private val builder = HomeScreenBuilder()
    private val json = Json { encodeDefaults = true }

    @Test
    fun `home screen has correct screenName and schemaVersion`() {
        val screen = builder.build(User("user_1", "Maria"), Account("acc_1", "user_1", BigDecimal("2500.00")))
        assertEquals("home", screen.screenName)
        assertEquals("v1", screen.schemaVersion)
    }

    @Test
    fun `home screen contains header, balance_card and shortcut_grid`() {
        val screen = builder.build(User("user_1", "Maria"), Account("acc_1", "user_1", BigDecimal("2500.00")))
        val types = screen.components.map { it.type }
        assertTrue("header" in types)
        assertTrue("balance_card" in types)
        assertTrue("shortcut_grid" in types)
    }

    @Test
    fun `header component has critical flag`() {
        val screen = builder.build(User("user_1", "Maria"), Account("acc_1", "user_1", BigDecimal("2500.00")))
        val header = screen.components.first { it.type == "header" }
        assertTrue(header.critical)
    }

    @Test
    fun `serializes to valid JSON with required fields`() {
        val screen = builder.build(User("user_1", "Maria"), Account("acc_1", "user_1", BigDecimal("2500.00")))
        val payload = json.encodeToString(screen)
        assertTrue(payload.contains("\"screenName\":\"home\""))
        assertTrue(payload.contains("\"schemaVersion\":\"v1\""))
        assertTrue(payload.contains("\"components\":["))
    }
}
