package com.hentech.sdui.application.port

import com.hentech.sdui.domain.entity.Contact

interface ContactRepository {
    fun findAll(): List<Contact>
    fun findById(id: String): Contact?
}
