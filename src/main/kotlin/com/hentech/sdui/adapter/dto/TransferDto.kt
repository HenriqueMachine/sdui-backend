package com.hentech.sdui.adapter.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransferRequestDto(val contactId: String, val amount: String)

@Serializable
data class TransferResponseDto(val result: TransferResultDto, val nextScreen: ScreenDto)

@Serializable
data class TransferResultDto(
    val success: Boolean,
    val transactionId: String? = null,
    val errorMessage: String? = null
)
