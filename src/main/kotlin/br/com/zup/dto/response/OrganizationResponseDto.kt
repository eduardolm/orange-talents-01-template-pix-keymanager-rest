package br.com.zup.dto.response

import br.com.zup.model.Organization

data class OrganizationResponseDto(val nome: String, val ispb: String) {

    fun toModel(): Organization {
        return Organization(nome, ispb)
    }
}
