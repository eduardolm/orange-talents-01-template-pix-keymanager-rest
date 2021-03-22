package br.com.zup.dto.request

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class KeyRequestByIdDtoTest {

    @Inject
    lateinit var validator: Validator

    @Test
    fun `test validator when no pixId or pixKey informed`() {

        val actual = KeyRequestByIdDto("", null)

        val exception = validator.validate(actual)

        assertEquals(1, exception.size)
    }

    @Test
    fun `test create instance when both pixId and clientId are passed to KeyRequestByIdDto constructor`() {
        val actual = KeyRequestByIdDto("Test", "Test")

        val validationResult = validator.validate(actual)

        assertEquals(0, validationResult.size)
    }

    @Test
    fun `test KeyRequestByIdDto toString`() {
        val actual = KeyRequestByIdDto("Test", "Type")

        assertEquals("KeyRequestByIdDto(id=Test, clientId=Type)", actual.toString())
    }
}