package br.com.zup.dto.request

import br.com.zup.validators.constraints.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class KeyDeleteRequestDto(
    @field: NotBlank(message = "É obrigatório informar o PixId.")
    @field: ValidUUID(message = "Não é um formato válido de UUID.")
    val pixId: String,
    @field: NotBlank(message = "Obrigatório informar o ClientId.")
    @field: ValidUUID(message = "Não é um formato válido de UUID.")
    val clientId: String
)
