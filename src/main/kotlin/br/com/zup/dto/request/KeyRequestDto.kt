package br.com.zup.dto.request

import br.com.zup.validators.constraints.Key
import br.com.zup.validators.constraints.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
@Key
data class KeyRequestDto(

    @field:NotBlank(message = "Id é obrigatório.")
    @field: ValidUUID(message = "Não é um formato válido de UUID.")
    val id: String,

    @field:NotBlank(message = "O tipo de chave é obrigatório.")
    val keyType: String,

    @field:Size(max = 77, message = "Tamanho máximo da chave: 77 caracteres.")
    var key: String?,

    @field:NotBlank(message = "É obrigatório informar o tipo de conta.")
    val accountType: String
) {

}

