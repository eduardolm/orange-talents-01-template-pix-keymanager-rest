package br.com.zup.dto.request

import javax.validation.constraints.NotBlank

data class KeyRequestDto(
    val id: String,
    val keyType: String,
    val key: String?,
    val accountType: String
) {

}