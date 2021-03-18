package br.com.zup.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class KeyDeleteRequestDto(
    @field: NotBlank(message = "É obrigatório informar o PixID.")
    val pixId: String,
    @field: NotBlank(message = "Obrigatório informar o ClientId.")
    val clientId: String
)
