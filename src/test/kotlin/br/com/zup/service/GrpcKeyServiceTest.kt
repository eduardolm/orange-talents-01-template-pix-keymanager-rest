package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.KeyDeleteRequestDto
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.dto.response.AccountReturnDto
import br.com.zup.dto.response.ClientResponseDto
import br.com.zup.dto.response.OrganizationResponseDto
import br.com.zup.dto.response.OwnerResponseDto
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import java.util.*

@MicronautTest
class GrpcKeyServiceTest {

    private lateinit var service: GrpcKeyService

    private lateinit var accountReturn: Optional<AccountReturnDto>
    private lateinit var client: Optional<ClientResponseDto>

    @Mock
    private lateinit var customerClient: CustomerClient
    private lateinit var owner: OwnerResponseDto
    private lateinit var institution: OrganizationResponseDto

    @MockBean(CustomerClient::class)
    fun customerClient(): CustomerClient {
        return mock(CustomerClient::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        institution = OrganizationResponseDto("ITAÚ UNIBANCO S.A.", "60701190")
        owner = OwnerResponseDto(
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f", "Alberto Tavares",
            "06628726061"
        )

        accountReturn = Optional.of(
            AccountReturnDto(
                "CONTA_CORRENTE", institution, "0001",
                "212233", owner
            )
        )
    }

    @Test
    fun `test buildCreateKeyResponse`() {
        service = GrpcKeyService(customerClient)

        val keyResponse = KeyResponseRest.newBuilder()
            .setClientId("clientId")
            .setPixId("pixId")
            .setCreatedAt(
                LocalDateTime.of(2021,
                5, 18, 22, 35).toString())
            .build()

        val actual = service.buildCreateKeyResponse(keyResponse)

        assertEquals("clientId", actual.clientId)
        assertEquals("pixId", actual.pixId)
        assertEquals("2021-05-18T22:35", actual.createdAt)
    }

    @Test
    fun `test buildCreateKeyRequest with valid clientId and accountType`() {
        service = GrpcKeyService(customerClient)

        client = Optional.of(ClientResponseDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "Alberto Tavares",
            "06628726061", institution))

        Mockito.`when`(customerClient.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(accountReturn)

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(client)

        val keyRequest = KeyRequestDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "CPF", "06628726061",
            "CONTA_CORRENTE")

        val actual = service.buildCreateKeyRequest(keyRequest)

        Mockito.verify(customerClient).findById(keyRequest.id)

        assertEquals("CPF", actual?.keyType.toString())
        assertEquals("06628726061", actual?.key)
        assertEquals(accountReturn.get().agencia, actual?.bankAccount?.branch)
        assertEquals(accountReturn.get().numero, actual?.bankAccount?.accountNumber)
        assertEquals(accountReturn.get().tipo, actual?.bankAccount?.accountType)
        assertEquals(accountReturn.get().instituicao.nome, actual?.bankAccount?.institution?.name)
        assertEquals(accountReturn.get().instituicao.ispb, actual?.bankAccount?.institution?.participant)
        assertEquals(accountReturn.get().titular.id, actual?.owner?.id)
        assertEquals(accountReturn.get().titular.nome, actual?.owner?.name)
        assertEquals(accountReturn.get().titular.cpf, actual?.owner?.taxIdNumber)
    }

    @Test
    fun `test buildCreateKeyRequest with invalid clientId and valid accountType`() {
        service = GrpcKeyService(customerClient)

        client = Optional.of(ClientResponseDto("0d1bb194-3c52-4e67-8c35-a93c0af92841", "Alberto Tavares",
            "06628726061", institution))

        Mockito.`when`(customerClient.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(accountReturn)

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(Optional.empty())

        val keyRequest = KeyRequestDto("0d1bb194-3c52-4e67-8c35-a93c0af92841", "CPF", "06628726061",
            "CONTA_CORRENTE")

        assertThrows(HttpStatusException::class.java) { service.buildCreateKeyRequest(keyRequest) }

        Mockito.verify(customerClient).findById(keyRequest.id)
    }

    @Test
    fun `test buildDeleteKeyRequest`() {
        service = GrpcKeyService(customerClient)

        val deleteRequest = KeyDeleteRequestDto("06628726061", "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val actual = service.buildDeleteKeyRequest(deleteRequest)

        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.clientId)
        assertEquals("06628726061", actual.pixId)
    }

    @Test
    fun `test buildDeleteResponse`() {
        service = GrpcKeyService(customerClient)

        val removeResponse = KeyRemoveResponse.newBuilder()
            .setKey("06628726061")
            .setParticipant("60701190")
            .setDeletedAt("2021-02-15T15:32")
            .build()

        val actual = service.buildDeleteKeyResponse(removeResponse)

        assertEquals("06628726061", actual.pixKey)
        assertEquals("60701190", actual.participant)
        assertEquals("2021-02-15T15:32", actual.deletedAt)
    }

    @Test
    fun `test buildPixKeyRequest with pixId and clientId`() {
        service = GrpcKeyService(customerClient)

        val keyRequestByIdDto = KeyRequestByIdDto("0b7338b0-f084-407a-a7ca-84d3873c49d8",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val actual = service.buildPixKeyRequest(keyRequestByIdDto)

        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.clientId)
        assertEquals("0b7338b0-f084-407a-a7ca-84d3873c49d8", actual.pixKey)
    }

    @Test
    fun `test buildPixKeyRequest with pixKey and null clientId`() {
        service = GrpcKeyService(customerClient)

        val keyRequestByIdDto = KeyRequestByIdDto("0b7338b0-f084-407a-a7ca-84d3873c49d8", null)

        val actual = service.buildPixKeyRequest(keyRequestByIdDto)

        assertEquals("", actual.clientId)
        assertEquals("0b7338b0-f084-407a-a7ca-84d3873c49d8", actual.pixKey)
        assertNull(keyRequestByIdDto.clientId)
    }

    @Test
    fun `test buildFindByIdKeyResponse`() {
        service = GrpcKeyService(customerClient)

        val pixKeyResponse = PixKeyResponse.newBuilder()
            .setPixId("pixId")
            .setClientId("clientId")
            .setKeyType(KeyType.valueOf("CPF"))
            .setPixKey("pixKey")
            .setOwner(ResponseOwner.newBuilder()
                .setName("Test name")
                .setTaxIdNumber("32165468794")
                .build())
            .setAccount(BankAccount.newBuilder()
                .setBranch("00002")
                .setAccountNumber("1234567")
                .setAccountType(AccountType.CONTA_POUPANCA.toString())
                .setInstitution(Institution.newBuilder()
                    .setName("Bank Name")
                    .setParticipant("3216547")
                    .build())
                .build())
            .setCreatedAt("2021-02-15T12:22")
            .build()

        val actual = service.buildFindByIdKeyResponse(pixKeyResponse)

        assertEquals("pixId", actual.pixId)
        assertEquals("clientId", actual.clientId)
        assertEquals("CPF", actual.keyType.toString())
        assertEquals("pixKey", actual.pixKey)
        assertEquals("Test name", actual.owner.ownerName)
        assertEquals("32165468794", actual.owner.ownerDocument)
        assertEquals("00002", actual.bankAccount.accountBranch)
        assertEquals("1234567", actual.bankAccount.accountNumber)
        assertEquals("CONTA_POUPANCA", actual.bankAccount.accountType)
        assertEquals("Bank Name", actual.bankAccount.accountInstitution.name)
        assertEquals("3216547", actual.bankAccount.accountInstitution.ispb)
        assertEquals("2021-02-15T12:22", actual.createdAt)
    }

    @Test
    fun `test buildListResponse`() {
        service = GrpcKeyService(customerClient)

        val keyResponseList = KeyListResponse.newBuilder()
            .addPixKeys(KeyListResponse.PixKeyItem.newBuilder()
                .setPixId("pixId")
                .setKeyType(KeyType.CPF)
                .setPixKey("pixKey")
                .setAccountType(AccountType.CONTA_POUPANCA)
                .setCreatedAt("2021-02-15T12:22")
                .build())
            .addPixKeys( KeyListResponse.PixKeyItem.newBuilder()
                .setPixId("pixId2")
                .setKeyType(KeyType.EMAIL)
                .setPixKey("test@email.com")
                .setAccountType(AccountType.CONTA_POUPANCA)
                .setCreatedAt("2021-02-15T12:22")
                .build())
            .setClientId("clientId")
            .build()

        val actual = service.buildListResponse(keyResponseList)

        assertEquals("clientId", actual?.clientId)
        assertEquals("pixId", actual?.keyList?.get(0)?.pixId)
        assertEquals("CPF", actual?.keyList?.get(0)?.keyType)
        assertEquals("pixKey", actual?.keyList?.get(0)?.pixKey)
        assertEquals("CONTA_POUPANCA", actual?.keyList?.get(0)?.accountType.toString())
        assertEquals("2021-02-15T12:22", actual?.keyList?.get(0)?.createdAt)
        assertEquals(2, actual?.keyList?.size)
    }
}
