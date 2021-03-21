package br.com.zup.dto.response

import br.com.zup.KeyType
import br.com.zup.model.Account
import br.com.zup.model.Owner

data class KeyResponseByIdDto(
        val pixId: String,
        val clientId: String,
        val keyType: KeyType,
        val pixKey: String,
        val bankAccount: Account,
        val owner: Owner,
        val createdAt: String,
){}

