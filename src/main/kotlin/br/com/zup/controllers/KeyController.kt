package br.com.zup.controllers

import br.com.zup.KeyServiceGrpc
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.service.GrpcKeyService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/v1/pix/keys")
class KeyController(
    @Inject val gRpcClient: KeyServiceGrpc.KeyServiceBlockingStub,
    @Inject val keyService: GrpcKeyService
) {

    @Post
    fun create(@Body requestDto: KeyRequestDto): HttpResponse<Any> {

        with(requestDto) {
            keyService.buildGrpcRequest(this)
        }?.let { gRpcClient.create(it) }?.let { return HttpResponse.created(keyService.buildResponse(it)) }

        return HttpResponse.notFound()
    }
}