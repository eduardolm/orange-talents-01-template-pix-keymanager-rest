package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
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

        assertEquals("123456789", bank.participant)
        assertEquals("0002", bank.branch)
        assertEquals("3216547", bank.accountNumber)
        assertEquals("CONTA_CORRENTE", bank.accountType)
    }

    @Test
    fun `test toString`() {
        val bank: Bank = Bank(
            participant = "123456789",
            branch = "0002",
            accountNumber = "3216547",
            accountType = "CONTA_CORRENTE"
        )

        assertEquals("Bank(participant='123456789', branch='0002', accountNumber='3216547', " +
                "accountType='CONTA_CORRENTE')", bank.toString())
    }
}