package com.hentech.sdui.infrastructure.route

import com.hentech.sdui.adapter.screen.*
import com.hentech.sdui.application.usecase.*
import com.hentech.sdui.infrastructure.validator.SchemaValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.screenRoutes(
    getUserUseCase: GetUserUseCase,
    getAccountUseCase: GetAccountUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    getContactsUseCase: GetContactsUseCase,
    schemaValidator: SchemaValidator,
    json: Json
) {
    val homeBuilder = HomeScreenBuilder()
    val extractBuilder = ExtractScreenBuilder()
    val transferBuilder = TransferScreenBuilder()
    val confirmBuilder = TransferConfirmScreenBuilder()

    route("/screens") {
        get("/home") {
            val userId = call.request.headers["X-User-Id"] ?: "user_1"
            val user = getUserUseCase.execute(userId)
            val account = getAccountUseCase.execute(userId)
            val screen = homeBuilder.build(user, account)
            val payload = json.encodeToString(screen)
            schemaValidator.validate(payload)
            call.respondText(payload, ContentType.Application.Json)
        }

        get("/extract") {
            val userId = call.request.headers["X-User-Id"] ?: "user_1"
            val user = getUserUseCase.execute(userId)
            val transactions = getTransactionsUseCase.execute(userId)
            val screen = extractBuilder.build(user, transactions)
            val payload = json.encodeToString(screen)
            schemaValidator.validate(payload)
            call.respondText(payload, ContentType.Application.Json)
        }

        get("/transfer") {
            val contacts = getContactsUseCase.execute()
            val screen = transferBuilder.build(contacts)
            val payload = json.encodeToString(screen)
            schemaValidator.validate(payload)
            call.respondText(payload, ContentType.Application.Json)
        }

        get("/transfer_confirm") {
            val contactId = call.request.queryParameters["contactId"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "contactId required"))
            val amountStr = call.request.queryParameters["amount"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "amount required"))
            val contacts = getContactsUseCase.execute()
            val contact = contacts.find { it.id == contactId }
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Contact not found"))
            val amount = amountStr.toBigDecimalOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid amount"))
            val screen = confirmBuilder.build(contact, amount)
            val payload = json.encodeToString(screen)
            schemaValidator.validate(payload)
            call.respondText(payload, ContentType.Application.Json)
        }
    }
}
