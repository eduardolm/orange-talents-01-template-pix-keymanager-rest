package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountReturnDtoTest {

    @Test
    fun `test create AccountReturnDto instance`() {
        val organization = OrganizationResponseDto("Organization", "321654987")
        val ownerResponse = OwnerResponseDto("32165", "Test Owner", "32165498714")

        val actual = AccountReturnDto("Type", organization, "0001", "32154", ownerResponse)

        assertEquals("Type", actual.tipo)
        assertEquals("0001", actual.agencia)
        assertEquals("32154", actual.numero)
        assertEquals("Organization", actual.instituicao.nome)
        assertEquals("321654987", actual.instituicao.ispb)
        assertEquals("32165", actual.titular.id)
        assertEquals("Test Owner", actual.titular.nome)
        assertEquals("32165498714", actual.titular.cpf)
    }

    @Test
    fun `test AccountReturnDto toString`() {
        val organization = OrganizationResponseDto("Organization", "321654987")
        val ownerResponse = OwnerResponseDto("32165", "Test Owner", "32165498714")

        val actual = AccountReturnDto("Type", organization, "0001", "32154", ownerResponse)

        assertEquals("AccountReturnDto(tipo=Type, instituicao=OrganizationResponseDto(nome=Organization, " +
                "ispb=321654987), agencia=0001, numero=32154, titular=OwnerResponseDto(id=32165, nome=Test Owner, " +
                "cpf=32165498714))", actual.toString())
    }
}