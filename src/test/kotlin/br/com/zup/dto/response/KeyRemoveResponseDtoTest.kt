package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KeyRemoveResponseDtoTest {

    @Test
    fun `test create KeyRemoveResponseDto instance`() {
        val actual = KeyRemoveResponseDto("test-key", "321654", LocalDateTime.of(2021,
            5, 18, 22, 35).toString())

        assertEquals("test-key", actual.pixKey)
        assertEquals("321654", actual.participant)
        assertEquals("2021-05-18T22:35", actual.deletedAt)
    }

    @Test
    fun `test KeyRemoveResponseDto toString`() {
        val actual = KeyRemoveResponseDto("test-key", "321654", LocalDateTime.of(2021,
            5, 18, 22, 35).toString())

        assertEquals( "KeyRemoveResponseDto(pixKey=test-key, participant=321654, deletedAt=2021-05-18T22:35)", actual.toString())
    }
}