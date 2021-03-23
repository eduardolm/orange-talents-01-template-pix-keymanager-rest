package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.KeyDeleteRequestDto
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.dto.response.*
import br.com.zup.model.Account
import br.com.zup.model.Organization
import io.micronaut.grpc.annotation.GrpcService
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

@GrpcService
class GrpcKeyService(@Inject val customerClient: CustomerClient) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun buildCreateKeyResponse(response: KeyResponseRest) = KeyResponseDto(
        clientId = response.clientId,
        pixId = response.pixId,
        createdAt = response.createdAt
    )

    fun buildCreateKeyRequest(requestDto: KeyRequestDto): KeyRequestRest? {

        if (customerClient.findById(requestDto.id).isEmpty) {

            logger.warn("Conta para o clientId: ${requestDto.id} não encontrada.")
            throw HttpStatusException(HttpStatus.NOT_FOUND, "Conta para o clientId: ${requestDto.id} não encontrada.")
        }

        return customerClient.findByIdAndAccountType(requestDto.id, requestDto.accountType)
            ?.map {
                KeyRequestRest.newBuilder()
                    .setKeyType(KeyType.valueOf(requestDto.keyType))
                    .setKey(requestDto.key)
                    .setBankAccount(
                        BankAccount.newBuilder()
                            .setBranch(it.agencia)
                            .setAccountNumber(it.numero)
                            .setAccountType(it.tipo)
                            .setInstitution(
                                Institution.newBuilder()
                                    .setName(it.instituicao.nome)
                                    .setParticipant(it.instituicao.ispb)
                                    .build()
                            )
                            .build()
                    )
                    .setOwner(
                        Owner.newBuilder()
                            .setId(it.titular.id)
                            .setName(it.titular.nome)
                            .setTaxIdNumber(it.titular.cpf)
                            .build()
                    )
                    .build()
            }
            ?.orElseGet { null }
    }

    fun buildDeleteKeyRequest(keyDeleteRequestDto: KeyDeleteRequestDto): KeyRemoveRequest {
        return KeyRemoveRequest.newBuilder()
            .setPixId(keyDeleteRequestDto.pixId)
            .setClientId(keyDeleteRequestDto.clientId)
            .build()
    }

    fun buildDeleteKeyResponse(keyRemoveResponse: KeyRemoveResponse): KeyRemoveResponseDto {
        return KeyRemoveResponseDto(
            pixKey = keyRemoveResponse.key,
            participant = keyRemoveResponse.participant,
            deletedAt = keyRemoveResponse.deletedAt
        )
    }

    fun buildPixKeyRequest(request: KeyRequestByIdDto): PixKeyRequest {

        return PixKeyRequest.newBuilder()
            .setPixKey(request.id)
            .setClientId(request.clientId ?: "")
            .build()
    }

    fun buildFindByIdKeyResponse(keyResponseById: PixKeyResponse): KeyResponseByIdDto {

        return KeyResponseByIdDto(
            pixId = keyResponseById.pixId,
            clientId = keyResponseById.clientId,
            keyType = keyResponseById.keyType,
            pixKey = keyResponseById.pixKey,
            bankAccount = Account(
                keyResponseById.account.accountType,
                Organization(
                    keyResponseById.account.institution.name,
                    keyResponseById.account.institution.participant
                ),
                keyResponseById.account.branch,
                keyResponseById.account.accountNumber),
            owner = br.com.zup.model.Owner(
                "",
                keyResponseById.owner.name,
                keyResponseById.owner.taxIdNumber),
            createdAt = keyResponseById.createdAt
        )
    }

    fun buildListResponse(keyResponseList: KeyListResponse?): PixKeyListByClientIdDto? {
        val listResponse: MutableList<PixListItemDto> = ArrayList()

        keyResponseList?.let { keyListResponse ->
            keyListResponse.pixKeysList.forEach {
                listResponse.add(PixListItemDto(
                    it.pixId,
                    it.keyType.toString(),
                    it.pixKey,
                    br.com.zup.enums.AccountType.valueOf(it.accountType.toString()),
                    it.createdAt
                ))
        }
        }

        return keyResponseList?.clientId?.let { PixKeyListByClientIdDto(clientId = it, keyList = listResponse) }
    }
}