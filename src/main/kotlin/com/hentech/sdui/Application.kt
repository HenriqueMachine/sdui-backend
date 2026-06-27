package com.hentech.sdui

import com.hentech.sdui.adapter.persistence.*
import com.hentech.sdui.application.usecase.*
import com.hentech.sdui.infrastructure.db.DatabaseFactory
import com.hentech.sdui.infrastructure.route.*
import com.hentech.sdui.infrastructure.validator.SchemaValidator
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val appJson = Json {
        encodeDefaults = true
        prettyPrint = false
        isLenient = true
    }

    install(ContentNegotiation) { json(appJson) }

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (cause.message ?: "Internal error")))
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (cause.message ?: "Unexpected error")))
        }
    }

    DatabaseFactory.init()

    val userRepo = ExposedUserRepository()
    val accountRepo = ExposedAccountRepository()
    val transactionRepo = ExposedTransactionRepository()
    val contactRepo = ExposedContactRepository()

    val getUserUseCase = GetUserUseCase(userRepo)
    val getAccountUseCase = GetAccountUseCase(accountRepo)
    val getTransactionsUseCase = GetTransactionsUseCase(transactionRepo)
    val getContactsUseCase = GetContactsUseCase(contactRepo)
    val transferMoneyUseCase = TransferMoneyUseCase(accountRepo, transactionRepo, contactRepo)

    val schemaValidator = SchemaValidator()

    routing {
        healthRoute()
        screenRoutes(getUserUseCase, getAccountUseCase, getTransactionsUseCase, getContactsUseCase, schemaValidator, appJson)
        transferRoutes(transferMoneyUseCase, getContactsUseCase, appJson)
    }
}
