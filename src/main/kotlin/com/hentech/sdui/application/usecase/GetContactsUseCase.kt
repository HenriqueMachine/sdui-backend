package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.ContactRepository
import com.hentech.sdui.domain.entity.Contact

class GetContactsUseCase(private val contactRepository: ContactRepository) {
    fun execute(): List<Contact> = contactRepository.findAll()
}
