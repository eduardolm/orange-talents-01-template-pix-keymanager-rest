package br.com.zup.controllers

import br.com.zup.KeyServiceGrpc
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.service.GrpcKeyService
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/pix/keys")
class KeyController(
    @Inject val gRpcClient: KeyServiceGrpc.KeyServiceBlockingStub,
    @Inject val keyService: GrpcKeyService
) {

    @Post
    fun create(@Body @Valid requestDto: KeyRequestDto): HttpResponse<Any> {

        with(requestDto) {
            keyService.buildGrpcRequest(this)
        }?.let {
            try {
                gRpcClient.create(it)
            } catch (e: StatusRuntimeException) {

                val status = e.status
                val statusCode = e.status.code
                val description =
                    if (status.description.equals("io exception"))
                        "Falha ao acessar o servidor." else status.description


                if (statusCode == Status.Code.ALREADY_EXISTS) {
                    throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
                } else {
                    throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, description)
                }
            }

        }?.let { return HttpResponse.created(keyService.buildResponse(it)) }

        return HttpResponse.notFound()
    }
}