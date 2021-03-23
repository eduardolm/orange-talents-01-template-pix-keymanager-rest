package br.com.zup.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountTypeTest {

    @Test
    fun `test AccountType enum`() {
        val test1 = AccountType.CONTA_CORRENTE
        val test2 = AccountType.CONTA_POUPANCA
        val test3 = AccountType.TIPO_DESCONHECIDO

        assertEquals("CONTA_CORRENTE", test1.toString())
        assertEquals("CONTA_POUPANCA", test2.toString())
        assertEquals("TIPO_DESCONHECIDO", test3.toString())
    }
}