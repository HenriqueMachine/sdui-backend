package com.hentech.sdui.infrastructure.route

import com.hentech.sdui.adapter.dto.TransferRequestDto
import com.hentech.sdui.adapter.dto.TransferResponseDto
import com.hentech.sdui.adapter.dto.TransferResultDto
import com.hentech.sdui.adapter.screen.TransferConfirmScreenBuilder
import com.hentech.sdui.adapter.screen.TransferSuccessScreenBuilder
import com.hentech.sdui.application.usecase.GetContactsUseCase
import com.hentech.sdui.application.usecase.TransferInput
import com.hentech.sdui.application.usecase.TransferMoneyUseCase
import com.hentech.sdui.domain.exception.InsufficientBalanceException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.transferRoutes(
    transferMoneyUseCase: TransferMoneyUseCase,
    getContactsUseCase: GetContactsUseCase,
    json: Json
) {
    val successBuilder = TransferSuccessScreenBuilder()
    val confirmBuilder = TransferConfirmScreenBuilder()

    post("/transfers") {
        val userId = call.request.headers["X-User-Id"] ?: "user_1"
        val request = call.receive<TransferRequestDto>()
        val amount = request.amount.toBigDecimalOrNull()
            ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid amount"))
        val contact = getContactsUseCase.execute().find { it.id == request.contactId }
            ?: return@post call.respond(HttpStatusCode.NotFound, mapOf("error" to "Contact not found"))

        try {
            val output = transferMoneyUseCase.execute(TransferInput(userId, request.contactId, amount))
            val nextScreen = successBuilder.build(output.transaction, output.newBalance, contact)
            val response = TransferResponseDto(
                result = TransferResultDto(success = true, transactionId = output.transaction.id),
                nextScreen = nextScreen
            )
            call.respondText(json.encodeToString(response), ContentType.Application.Json)
        } catch (e: InsufficientBalanceException) {
            val errorScreen = confirmBuilder.build(contact, amount, "Saldo insuficiente para realizar a transferência")
            val response = TransferResponseDto(
                result = TransferResultDto(success = false, errorMessage = e.message),
                nextScreen = errorScreen
            )
            call.respondText(json.encodeToString(response), ContentType.Application.Json, HttpStatusCode.UnprocessableEntity)
        }
    }
}
