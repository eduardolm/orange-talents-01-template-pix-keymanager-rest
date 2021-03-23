package br.com.zup.enums

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OwnerTypeTest {

    @Test
    fun `test OwnerType enum`() {
        val test1 = OwnerType.NATURAL_PERSON
        val test2 = OwnerType.LEGAL_PERSON

        Assertions.assertEquals("NATURAL_PERSON", test1.toString())
        Assertions.assertEquals("LEGAL_PERSON", test2.toString())

    }
}