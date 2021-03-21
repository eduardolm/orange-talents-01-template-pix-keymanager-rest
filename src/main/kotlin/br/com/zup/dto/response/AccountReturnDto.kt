package br.com.zup.dto.response

data class AccountReturnDto(
    val tipo: String,
    val instituicao: OrganizationResponseDto,
    val agencia: String,
    val numero: String,
    val titular: OwnerResponseDto
) {

}
