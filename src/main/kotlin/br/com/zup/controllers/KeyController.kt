package br.com.zup.controllers

import br.com.zup.KeyServiceGrpc
import br.com.zup.dto.request.KeyDeleteRequestDto
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.exception.BankAccountNotFoundException
import br.com.zup.service.GrpcKeyService
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/pix/keys")
class KeyController(
    @Inject val gRpcClient: KeyServiceGrpc.KeyServiceBlockingStub,
    @Inject val keyService: GrpcKeyService
) {

    private val logger: Logger = LoggerFactory.getLogger(KeyController::class.java)

    @Post
    fun create(@Body @Valid requestDto: KeyRequestDto): HttpResponse<Any> {

        logger.info("Cadastro de Chave Pix...")

        with(requestDto) {
            keyService.buildCreateKeyRequest(this)
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
                    logger.warn("Chave já cadastrada: ${requestDto.key}")
                    throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
                } else {
                    logger.error("Falha ao acessar o servidor: $description")
                    throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, description)
                }
            } catch (e: BankAccountNotFoundException) {

                logger.warn("Conta não encontrada.")
                throw HttpStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada.")
            }

        }?.let {
            logger.info("Chave Pix ${it.pixId} cadastrada com sucesso.")
            return HttpResponse.created(keyService.buildCreateKeyResponse(it)) }

        logger.warn("Conta não encontrada.")
        return HttpResponse.notFound()
    }

    @Delete
    fun delete(@Body @Valid keyDeleteRequestDto: KeyDeleteRequestDto): HttpResponse<Any?> {

        with(keyDeleteRequestDto) {
            keyService.buildDeleteKeyRequest(this)
        }.let {
            try {
                gRpcClient.delete(it)

            } catch (e: StatusRuntimeException) {

                val status = e.status
                val statusCode = e.status.code
                val description = status.description

                if (statusCode == Status.Code.NOT_FOUND) {
                    logger.error("Chave ${it.pixId} não encontrada ou não pertence ao usuário.")
                    throw HttpStatusException(HttpStatus.NOT_FOUND, description)

                } else {
                    logger.error("Falha ao acessar o servidor: ${e.message}")
                    throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao acessar o servidor.")
                }
            }
        }?.let {

            logger.info("Chave Pix ${it.key} removida com sucesso.")
            return HttpResponse.ok(keyService.buildDeleteKeyResponse(it))}

        return HttpResponse.notFound()
    }

    @Get("/{id}")
    fun findById(@PathVariable("id") id: String,
                 @QueryValue("clientId") clientId: String?): HttpResponse<Any?> {

        val request = KeyRequestByIdDto(id, clientId)

        with(request) {
            try {
                gRpcClient.retrievePixKey(keyService.buildPixKeyRequest(this))

            } catch (e: StatusRuntimeException) {

                val status = e.status
                val statusCode = e.status.code
                val description = status.description

                if (statusCode == Status.Code.NOT_FOUND) {
                    logger.error("Chave ${this.id} não encontrada.")
                    throw HttpStatusException(HttpStatus.NOT_FOUND, description)

                } else {
                    logger.error("Falha ao acessar o servidor: ${e.message}")
                    throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao acessar o servidor.")
                }
            }
        }?.let {

            logger.info("Chave Pix ${it.pixKey} retornada com sucesso.")
            return HttpResponse.ok(keyService.buildFindByIdKeyResponse(it))
        }

        return HttpResponse.notFound()
    }

}