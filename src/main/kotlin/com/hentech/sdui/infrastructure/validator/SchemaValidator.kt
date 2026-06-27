package com.hentech.sdui.infrastructure.validator

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.JsonSchema
import com.networknt.schema.SpecVersion
import java.io.File

class SchemaValidator(schemasPath: String = System.getenv("SCHEMAS_PATH") ?: "schemas/v1") {
    private val schema: JsonSchema
    private val mapper = ObjectMapper()

    init {
        val schemaFile = File("$schemasPath/screen.schema.json")
        val factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
        schema = factory.getSchema(schemaFile.toURI())
    }

    fun validate(json: String) {
        val node = mapper.readTree(json)
        val errors = schema.validate(node)
        if (errors.isNotEmpty()) {
            val messages = errors.joinToString("; ") { it.message }
            error("Schema validation failed: $messages")
        }
    }
}
