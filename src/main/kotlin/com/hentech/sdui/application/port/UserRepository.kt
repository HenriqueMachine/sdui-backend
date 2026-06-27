package com.hentech.sdui.application.port

import com.hentech.sdui.domain.entity.User

interface UserRepository {
    fun findById(id: String): User?
}
