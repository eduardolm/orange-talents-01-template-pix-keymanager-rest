package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrganizationTest {

    @Test
    fun `test constructor and getters for Organization`() {
        val organization: Organization = Organization(name = "Test", ispb = "3214565")

        assertEquals("Test", organization.name)
        assertEquals("3214565", organization.ispb)
    }
}