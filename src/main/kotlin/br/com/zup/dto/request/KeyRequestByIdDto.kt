package br.com.zup.dto.request

import br.com.zup.validators.constraints.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class KeyRequestByIdDto(
    @field:NotBlank(message = "Obrigatório informar o pixKey ou pixId")
    val id: String,
    @field: ValidUUID(message = "Não é um formato válido de UUID.")
    val clientId: String?
    )
{
}
