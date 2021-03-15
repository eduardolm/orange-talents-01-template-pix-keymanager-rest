package br.com.zup.service

import br.com.zup.BankAccount
import br.com.zup.Owner
import br.com.zup.KeyRequestRest
import br.com.zup.KeyResponseRest
import br.com.zup.KeyType
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.dto.response.AccountReturnDto
import br.com.zup.dto.response.KeyResponseDto
import br.com.zup.model.Account
import io.micronaut.grpc.annotation.GrpcService
import java.util.*
import javax.inject.Inject

@GrpcService
class GrpcKeyService(@Inject val customerClient: CustomerClient) {

    fun buildResponse(response: KeyResponseRest) = KeyResponseDto(
        clientId = response.clientId,
        pixId = response.pixId,
        createdAt = response.createdAt
    )

    fun buildGrpcRequest(requestDto: KeyRequestDto): KeyRequestRest? {
        val accountReturn: Optional<AccountReturnDto> =
            customerClient.findByIdAndAccountType(requestDto.id, requestDto.accountType)

        if (accountReturn.isPresent) {
            val account: Account = accountReturn.get().toModel()
            return KeyRequestRest.newBuilder()
                .setKeyType(KeyType.valueOf(requestDto.keyType))
                .setKey(requestDto.key)
                .setBankAccount(
                    BankAccount.newBuilder()
                        .setParticipant(account.accountInstitution.ispb)
                        .setBranch(account.accountBranch)
                        .setAccountNumber(account.accountNumber)
                        .setAccountType(account.accountType)
                        .build()
                )
                .setOwner(
                    Owner.newBuilder()
                        .setId((account.accountOwner.ownerId))
                        .setName(account.accountOwner.ownerName)
                        .setTaxIdNumber(account.accountOwner.ownerDocument)
                        .build()
                )
                .build()
        }
        return null
    }
}