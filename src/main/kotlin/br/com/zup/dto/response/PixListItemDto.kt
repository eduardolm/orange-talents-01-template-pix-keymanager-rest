package br.com.zup.dto.response

import br.com.zup.enums.AccountType

data class PixListItemDto(
    val pixId: String,
    val keyType: String,
    val pixKey: String,
    val accountType: AccountType,
    val createdAt: String
) {

}
