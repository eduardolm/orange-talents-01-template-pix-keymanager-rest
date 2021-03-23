package br.com.zup.dto.response

import br.com.zup.enums.AccountType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PixListItemDtoTest {

    @Test
    fun `test PixListItemDto constructor and getters`() {
        val pix = PixListItemDto("pixId", "KeyType", "pixKey", AccountType.CONTA_CORRENTE,
            LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        assertEquals("pixId", pix.pixId)
        assertEquals("KeyType", pix.keyType)
        assertEquals("pixKey", pix.pixKey)
        assertEquals("CONTA_CORRENTE", pix.accountType.toString())
        assertEquals("2021-05-18T22:35", pix.createdAt)
    }

    @Test
    fun `test PixListItemDto toString`() {
        val pix = PixListItemDto("pixId", "KeyType", "pixKey", AccountType.CONTA_CORRENTE,
            LocalDateTime.of(2021, 5, 18, 22, 35).toString())

        assertEquals("PixListItemDto(pixId=pixId, keyType=KeyType, pixKey=pixKey, accountType=CONTA_CORRENTE, createdAt=2021-05-18T22:35)", pix.toString())
    }
}