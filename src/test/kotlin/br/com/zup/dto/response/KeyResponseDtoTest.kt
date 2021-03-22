package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KeyResponseDtoTest {

    @Test
    fun `test constructor and getters for KeyResponseDto`() {
        val actual = KeyResponseDto("clientId", "pixId", LocalDateTime.of(2021, 5,
            18, 22, 35).toString())

        assertEquals("clientId", actual.clientId)
        assertEquals("pixId", actual.pixId)
        assertEquals("2021-05-18T22:35", actual.createdAt)
    }

    @Test
    fun `test KeyResponseDto toString`() {
        val actual = KeyResponseDto("clientId", "pixId", LocalDateTime.of(2021, 5,
            18, 22, 35).toString())

        assertEquals( "KeyResponseDto(clientId=clientId, pixId=pixId, createdAt=2021-05-18T22:35)",
            actual.toString())
    }
}