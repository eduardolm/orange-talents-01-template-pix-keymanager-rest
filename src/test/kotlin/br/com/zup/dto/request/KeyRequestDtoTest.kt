package br.com.zup.dto.request

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

@MicronautTest
class KeyRequestDtoTest {

    @Inject
    lateinit var validator: Validator

    @Test
    fun `test create KeyRequestDto with all parameters correctly set`() {
        val actual = KeyRequestDto(UUID.randomUUID().toString(), "Type", "Key", "Account")

        val validationResult = validator.validate(actual)

        assertEquals(0, validationResult.size)
    }

    @Test
    fun `test create KeyRequestDto with blank id`() {
        val actual = KeyRequestDto("", "Type", "Key", "Account")

        val validationResult = validator.validate(actual)

        assertEquals(2, validationResult.size)
    }

    @Test
    fun `test create KeyRequestDto with blank keyType`() {
        val actual = KeyRequestDto(UUID.randomUUID().toString(), "", "Key", "Account")

        val validationResult = validator.validate(actual)

        assertEquals(1, validationResult.size)
    }

    @Test
    fun `test create KeyRequestDto with blank key`() {
        val actual = KeyRequestDto("Test", "Type", "", "Account")

        val validationResult = validator.validate(actual)

        assertEquals(1, validationResult.size)
    }

    @Test
    fun `test create KeyRequestDto with blank account`() {
        val actual = KeyRequestDto(UUID.randomUUID().toString(), "Type", "Key", "")

        val validationResult = validator.validate(actual)

        assertEquals(1, validationResult.size)
    }

    @Test
    fun `test KeyRequestDto toString`() {
        val actual = KeyRequestDto("Test", "Type", "Key", "Account")

        assertEquals("KeyRequestDto(id=Test, keyType=Type, key=Key, accountType=Account)", actual.toString())
    }
}