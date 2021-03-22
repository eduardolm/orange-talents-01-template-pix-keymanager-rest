package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrganizationResponseDtoTest {

    @Test
    fun `test OrganizationResponseDto constructor and getters`() {
        val actual = OrganizationResponseDto("Test name", "321654")

        assertEquals("Test name", actual.nome)
        assertEquals("321654", actual.ispb)
    }

    @Test
    fun `test OrganizationResponseDto toString`() {
        val actual = OrganizationResponseDto("Test name", "321654")

        assertEquals( "OrganizationResponseDto(nome=Test name, ispb=321654)", actual.toString())
    }
}