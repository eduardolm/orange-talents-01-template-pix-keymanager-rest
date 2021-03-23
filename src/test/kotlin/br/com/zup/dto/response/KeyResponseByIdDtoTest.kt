package br.com.zup.dto.response

import br.com.zup.KeyType
import br.com.zup.model.Account
import br.com.zup.model.Organization
import br.com.zup.model.Owner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KeyResponseByIdDtoTest {

    @Test
    fun `test constructor and getter for KeyResponseByIdDto`() {
        val organization = Organization("Organization", "321654987")
        val ownerResponse = Owner("32165", "Test Owner", "32165498714")

        val account = Account("AccountType", organization, "accountBranch",
            "accountNumber")

        val actual = KeyResponseByIdDto("pixId", "clientId", KeyType.CPF, "pixKey",
            account, ownerResponse, LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        assertEquals("pixId", actual.pixId)
        assertEquals("clientId", actual.clientId)
        assertEquals("CPF", actual.keyType.toString())
        assertEquals("pixKey", actual.pixKey)
        assertEquals("Organization", actual.bankAccount.accountInstitution.name)
        assertEquals("321654987", actual.bankAccount.accountInstitution.ispb)
        assertEquals("AccountType", actual.bankAccount.accountType)
        assertEquals("accountBranch", actual.bankAccount.accountBranch)
        assertEquals("accountNumber", actual.bankAccount.accountNumber)
        assertEquals("32165", actual.owner.ownerId)
        assertEquals("Test Owner", actual.owner.ownerName)
        assertEquals("32165498714", actual.owner.ownerDocument)
        assertEquals("2021-05-18T22:35", actual.createdAt)
    }

    @Test
    fun `test KeyResponseByIdDto toString`() {
        val organization = Organization("Organization", "321654987")
        val ownerResponse = Owner("32165", "Test Owner", "32165498714")

        val account = Account("AccountType", organization, "accountBranch",
            "accountNumber")

        val actual = KeyResponseByIdDto("pixId", "clientId", KeyType.CPF, "pixKey",
            account, ownerResponse, LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        assertEquals("KeyResponseByIdDto(pixId=pixId, clientId=clientId, keyType=CPF, pixKey=pixKey, " +
                "bankAccount=Account(accountType=AccountType, accountInstitution=Organization(name=Organization, " +
                "ispb=321654987), accountBranch=accountBranch, accountNumber=accountNumber), owner=Owner(ownerId=" +
                "32165, ownerName=Test Owner, ownerDocument=32165498714), createdAt=2021-05-18T22:35)",
            actual.toString())
    }
}