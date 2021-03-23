package br.com.zup.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BankTest {

    @Test
    fun `test constructor and getters for Bank model`() {
        val bank: Bank = Bank(
            participant = "123456789",
            branch = "0002",
            accountNumber = "3216547",
            accountType = "CONTA_CORRENTE"
        )

        Assertions.assertEquals("123456789", bank.participant)
        Assertions.assertEquals("0002", bank.branch)
        Assertions.assertEquals("3216547", bank.accountNumber)
        Assertions.assertEquals("CONTA_CORRENTE", bank.accountType)
    }
}