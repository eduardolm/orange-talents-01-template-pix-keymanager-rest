package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountTest {

    @Test
    fun `test constructor and getters for Account model`() {
        val organization: Organization = Organization(
            name = "Test Bank",
            ispb = "3214567"
        )
        val account: Account = Account(
            accountType = "CACC",
            accountInstitution = organization,
            accountBranch = "0002",
            accountNumber = "123456"
        )

        assertEquals("CACC", account.accountType)
        assertEquals("Test Bank", account.accountInstitution.name)
        assertEquals("3214567", account.accountInstitution.ispb)
        assertEquals("0002", account.accountBranch)
        assertEquals("123456", account.accountNumber)
    }
}