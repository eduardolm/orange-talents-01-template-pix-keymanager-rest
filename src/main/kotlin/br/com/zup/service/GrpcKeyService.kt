package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.KeyDeleteRequestDto
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.dto.response.KeyRemoveResponseDto
import br.com.zup.dto.response.KeyResponseByIdDto
import br.com.zup.dto.response.KeyResponseDto
import br.com.zup.model.Account
import br.com.zup.model.Organization
import io.micronaut.grpc.annotation.GrpcService
import javax.inject.Inject

@GrpcService
class GrpcKeyService(@Inject val customerClient: CustomerClient) {

    fun buildCreateKeyResponse(response: KeyResponseRest) = KeyResponseDto(
        clientId = response.clientId,
        pixId = response.pixId,
        createdAt = response.createdAt
    )

    fun buildCreateKeyRequest(requestDto: KeyRequestDto): KeyRequestRest? {

        return customerClient.findByIdAndAccountType(requestDto.id, requestDto.accountType)
            .map {
                KeyRequestRest.newBuilder()
                    .setKeyType(KeyType.valueOf(requestDto.keyType))
                    .setKey(requestDto.key)
                    .setBankAccount(
                        BankAccount.newBuilder()
                            .setBranch(it.agencia)
                            .setAccountNumber(it.numero)
                            .setAccountType(it.tipo)
                            .setInstitution(Institution.newBuilder()
                                .setName(it.instituicao.nome)
                                .setParticipant(it.instituicao.ispb)
                                .build())
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
            .orElseGet { null }
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
}