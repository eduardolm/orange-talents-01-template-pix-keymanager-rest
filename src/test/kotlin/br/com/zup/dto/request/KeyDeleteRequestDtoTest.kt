package br.com.zup.dto.request

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

@MicronautTest
class KeyDeleteRequestDtoTest{

    @Inject
    lateinit var validator: Validator

    @Test
    fun `test create instance when both pixId and clientId exist`() {
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()

        val keyDeleteRequestDto = KeyDeleteRequestDto(pixId, clientId)

        assertEquals(pixId, keyDeleteRequestDto.pixId)
        assertEquals(clientId, keyDeleteRequestDto.clientId)
    }

    @Test
    fun `test create instance when pixId is blank`() {

        val clientId = UUID.randomUUID().toString()
        val actual = KeyDeleteRequestDto("", clientId)

        val exception = validator.validate(actual)

        assertEquals(2, exception.size)
    }

    @Test
    fun `test create instance whn clientId is blank`() {

        val pixId = UUID.randomUUID().toString()
        val actual = KeyDeleteRequestDto(pixId, "")

        val exception = validator.validate(actual)

        assertEquals(2, exception.size)
    }

    @Test
    fun `test create when both parameters are blank`() {

        val actual = KeyDeleteRequestDto("", "")

        val exception = validator.validate(actual)

        assertEquals(4, exception.size)
    }

    @Test
    fun `test KeyDeleteRequestDto toString`() {
        val actual = KeyDeleteRequestDto("Test", "Type")

        assertEquals("KeyDeleteRequestDto(pixId=Test, clientId=Type)", actual.toString())
    }
}