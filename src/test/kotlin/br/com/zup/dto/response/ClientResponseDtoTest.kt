package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClientResponseDtoTest {

    @Test
    fun `test constructor and getters for ClientResponseDto`() {
        val organization = OrganizationResponseDto("Test name", "321654")
        val actual = ClientResponseDto("clientId", "Client Name", "Client CPF", instituicao = organization)

        assertEquals("Test name", actual.instituicao.nome)
        assertEquals("321654", actual.instituicao.ispb)
        assertEquals("clientId", actual.id)
        assertEquals("Client Name", actual.nome)
        assertEquals("Client CPF", actual.cpf)
    }

    @Test
    fun `test ClientResponseDto toString`() {
        val organization = OrganizationResponseDto("Test name", "321654")
        val actual = ClientResponseDto("clientId", "Client Name", "Client CPF", instituicao = organization)

        assertEquals("ClientResponseDto(id=clientId, nome=Client Name, cpf=Client CPF, instituicao=" +
                "OrganizationResponseDto(nome=Test name, ispb=321654))", actual.toString())
    }
}