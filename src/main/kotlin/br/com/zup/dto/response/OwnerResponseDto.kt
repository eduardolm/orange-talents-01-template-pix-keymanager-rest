package br.com.zup.dto.response

import br.com.zup.model.Owner

data class OwnerResponseDto(val id: String, val nome: String, val cpf: String) {

    fun toModel(): Owner {
        return Owner(id, nome, cpf)
    }
}
