package br.com.zup.dto.response

import br.com.zup.enums.AccountType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PixKeyListByClientIdDtoTest {

    @Test
    fun `test PixKeyListByClientIdDto constructor and getters`() {
        val pix = PixListItemDto("pixId", "KeyType", "pixKey", AccountType.CONTA_CORRENTE,
            LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        val pix2 = PixListItemDto("pixId2", "KeyType2", "pixKey2", AccountType.CONTA_POUPANCA,
            LocalDateTime.of(2020, 10, 9, 5, 4).toString())

        val actual = listOf(pix, pix2)

        assertEquals(2, actual.size)
        assertEquals("pixId", actual.get(0).pixId)
        assertEquals("KeyType", actual.get(0).keyType)
        assertEquals("pixKey", actual.get(0).pixKey)
        assertEquals("CONTA_CORRENTE", actual.get(0).accountType.toString())
        assertEquals("2021-05-18T22:35", actual.get(0).createdAt)
    }

    @Test
    fun `test PixKeyListByClientIdDto toString`() {
        val pix = PixListItemDto("pixId", "KeyType", "pixKey", AccountType.CONTA_CORRENTE,
            LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        val pix2 = PixListItemDto("pixId2", "KeyType2", "pixKey2", AccountType.CONTA_POUPANCA,
            LocalDateTime.of(2020, 10, 9, 5, 4).toString())

        val actual = listOf(pix, pix2)

        assertEquals("[PixListItemDto(pixId=pixId, keyType=KeyType, pixKey=pixKey, accountType=" +
                "CONTA_CORRENTE, createdAt=2021-05-18T22:35), PixListItemDto(pixId=pixId2, keyType=KeyType2, " +
                "pixKey=pixKey2, accountType=CONTA_POUPANCA, createdAt=2020-10-09T05:04)]", actual.toString())
    }
}