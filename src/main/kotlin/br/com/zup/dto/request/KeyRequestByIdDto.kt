package br.com.zup.dto.request

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class KeyRequestByIdDto(
    @field:NotBlank(message = "Obrigatório informar o pixKey ou pixId")
    val id: String,
    val clientId: String?)
{
}
