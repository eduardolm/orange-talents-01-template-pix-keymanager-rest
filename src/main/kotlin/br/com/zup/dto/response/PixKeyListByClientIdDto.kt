package br.com.zup.dto.response

data class PixKeyListByClientIdDto(
    val clientId: String,
    val keyList: List<PixListItemDto>? = emptyList()
)