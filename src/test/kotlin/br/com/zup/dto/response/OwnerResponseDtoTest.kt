package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OwnerResponseDtoTest {

    @Test
    fun `test OwnerResponseDto constructor and getters`() {
        val actual = OwnerResponseDto("TestId", "Test Name", "Test CPF")

        assertEquals("TestId", actual.id)
        assertEquals("Test Name", actual.nome)
        assertEquals("Test CPF", actual.cpf)
    }

    @Test
    fun `test OwnerResponseDto toString`() {
        val actual = OwnerResponseDto("TestId", "Test Name", "Test CPF")

        assertEquals("OwnerResponseDto(id=TestId, nome=Test Name, cpf=Test CPF)", actual.toString())
    }
}