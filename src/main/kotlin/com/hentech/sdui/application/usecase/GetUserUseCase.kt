package com.hentech.sdui.application.usecase

import com.hentech.sdui.application.port.UserRepository
import com.hentech.sdui.domain.entity.User

class GetUserUseCase(private val userRepository: UserRepository) {
    fun execute(userId: String): User =
        userRepository.findById(userId) ?: error("User not found: $userId")
}
