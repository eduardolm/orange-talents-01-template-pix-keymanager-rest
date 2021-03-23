package br.com.zup.service

import br.com.zup.dto.response.AccountReturnDto
import br.com.zup.dto.response.ClientResponseDto
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import java.util.*

@Client("http://localhost:9091")
interface CustomerClient {

    @Get( "/api/v1/clientes/{id}/contas?tipo={tipo}")
    fun findByIdAndAccountType(id: String, tipo: String?): Optional<AccountReturnDto>?

    @Get("/api/v1/clientes/{clientId}")
    fun findById(clientId: String): Optional<ClientResponseDto>
}