package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OwnerTest {

    @Test
    fun `test constructor and getters for Owner`() {
        val owner: Owner = Owner(ownerId = "321654987", ownerName = "Test Owner", ownerDocument = "32165489745")

        assertEquals("321654987", owner.ownerId)
        assertEquals("Test Owner", owner.ownerName)
        assertEquals("32165489745", owner.ownerDocument)

    }
}