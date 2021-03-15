package br.com.zup.dto.response

import br.com.zup.model.Account

data class AccountReturnDto(val tipo: String, val instituicao: OrganizationResponseDto, val agencia: String, val numero: String, val titular: OwnerResponseDto) {

    fun toModel(): Account {
        return Account(tipo, instituicao.toModel(), agencia, numero, titular.toModel())
    }
}
