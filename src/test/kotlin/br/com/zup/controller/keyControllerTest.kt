package br.com.zup.controller

import br.com.zup.*
import br.com.zup.controllers.KeyController
import br.com.zup.dto.request.KeyDeleteRequestDto
import br.com.zup.dto.request.KeyRequestDto
import br.com.zup.dto.response.*
import br.com.zup.service.CustomerClient
import br.com.zup.service.GrpcKeyService
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.grpcmock.GrpcMock.*
import org.grpcmock.junit5.GrpcMockExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@ExtendWith(GrpcMockExtension::class)
@MicronautTest
class KeyControllerTest {

    private lateinit var channel: ManagedChannel

    private lateinit var gRpcClient: KeyServiceGrpc.KeyServiceBlockingStub

    private lateinit var keyService: GrpcKeyService

    @Mock
    private lateinit var customerClient: CustomerClient

    private lateinit var owner: OwnerResponseDto
    private lateinit var institution: OrganizationResponseDto
    private lateinit var accountReturn: Optional<AccountReturnDto>
    private lateinit var client: Optional<ClientResponseDto>
    private lateinit var request: KeyRequestDto
    private lateinit var requestRest: KeyRequestRest
    private lateinit var responseRest: KeyResponseRest
    private lateinit var responseDto: KeyResponseDto

    @MockBean(CustomerClient::class)
    fun customerClient(): CustomerClient {
        return Mockito.mock(CustomerClient::class.java)
    }

    @BeforeEach
    fun setup() {

        channel = ManagedChannelBuilder.forAddress("localhost", getGlobalPort())
            .usePlaintext()
            .build()
        gRpcClient = KeyServiceGrpc.newBlockingStub(channel)

        MockitoAnnotations.openMocks(this)
        keyService = GrpcKeyService(customerClient)

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

        client = Optional.of(ClientResponseDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "Alberto Tavares",
            "06628726061", institution))

        // RequestDTO
        request = KeyRequestDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "CPF", "06628726061",
            "CONTA_CORRENTE")

        // gRPC Request
        requestRest = KeyRequestRest.newBuilder()
            .setKeyType(KeyType.valueOf("CPF"))
            .setKey("06628726061")
            .setBankAccount(BankAccount.newBuilder()
                .setBranch("0002")
                .setAccountNumber("123456")
                .setAccountType("CONTA_CORRENTE")
                .setInstitution(Institution.newBuilder()
                    .setName("ITAÚ UNIBANCO S.A.")
                    .setParticipant("60701190")
                    .build())
                .build())
            .setOwner(Owner.newBuilder()
                .setId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
                .setName("Alberto Tavares")
                .setTaxIdNumber("06628726061")
                .build())
            .build()

        // GrpcResponse
        responseRest = KeyResponseRest.newBuilder()
            .setClientId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .setPixId("06628726061")
            .setCreatedAt("2021-03-24T13:51")
            .build()

        responseDto = KeyResponseDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "06628726061",
            "2021-03-24T13:51")
    }

    @AfterEach
    fun cleanup() {
        Optional.ofNullable(channel).ifPresent(ManagedChannel::shutdownNow)
    }

    @Test
    fun `test create with valid request`() {

        stubFor(unaryMethod(KeyServiceGrpc.getCreateMethod()).willReturn(responseRest))
        Mockito.`when`(customerClient.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(accountReturn)

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(client)

        val controller = KeyController(gRpcClient, keyService)
        val testResult = controller.create(request)

        assertEquals(gRpcClient.create(requestRest), responseRest)
        verifyThat(calledMethod(KeyServiceGrpc.getCreateMethod()).withRequest(requestRest))
        assertEquals(HttpStatus.CREATED, testResult.status)
        assertEquals(responseDto, testResult.body.get())
    }

    @Test
    fun `test create with invalid clientID should throw HttpStatusException`() {

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(Optional.empty())

        val controller = KeyController(gRpcClient, keyService)

        assertThrows(HttpStatusException::class.java) {controller.create(request)}
    }

    @Test
    fun `test create without passing clientId on the request should throw HttpStatusException`() {

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(Optional.empty())

        request = KeyRequestDto("", "CPF", "06628726061",
            "CONTA_CORRENTE")
        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.create(request)}
    }

    @Test
    fun `test create passing already registered pixKey should throw HttpStatusException`() {

        stubFor(unaryMethod(KeyServiceGrpc.getCreateMethod()).willReturn(Status.ALREADY_EXISTS))
        Mockito.`when`(customerClient.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(accountReturn)

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(client)

        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.create(request)}
    }

    @Test
    fun `test create when gRPC server is unavaliable`() {
        stubFor(unaryMethod(KeyServiceGrpc.getCreateMethod()).willReturn(Status.UNAVAILABLE))
        Mockito.`when`(customerClient.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(accountReturn)

        Mockito.`when`(customerClient.findById(Mockito.anyString())).thenReturn(client)

        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.create(request)}
    }

    @Test
    fun `test delete pixKey with valid payload`() {

        val deleteRequest = KeyDeleteRequestDto("06628726061", "0d1bb194-3c52-4e67-8c35-a93c0af9284f")
        val deleteRequestGrpc = KeyRemoveRequest.newBuilder()
            .setPixId(deleteRequest.pixId)
            .setClientId(deleteRequest.clientId)
            .build()

        val deleteResponse = KeyRemoveResponse.newBuilder()
            .setKey("06628726061")
            .setParticipant("60701190")
            .setDeletedAt("2021-03-24T22:28")
            .build()

        val deleteResponseDto = KeyRemoveResponseDto(deleteResponse.key, deleteResponse.participant,
            deleteResponse.deletedAt)

        stubFor(unaryMethod(KeyServiceGrpc.getDeleteMethod()).willReturn(deleteResponse))

        val controller = KeyController(gRpcClient, keyService)
        val result = controller.delete(deleteRequest)

        assertEquals(HttpStatus.OK, result.status)
        verifyThat(calledMethod(KeyServiceGrpc.getDeleteMethod()).withRequest(deleteRequestGrpc))
        assertEquals(gRpcClient.delete(deleteRequestGrpc), deleteResponse)
        assertEquals(deleteResponseDto, result.body.get())
    }

    @Test
    fun `test delete pixKey with incorrect clientId`() {
        val deleteRequest = KeyDeleteRequestDto("06628726061", "0d1bb194-3c52-4e67-8c35-a93c0af92841")

        stubFor(unaryMethod(KeyServiceGrpc.getDeleteMethod()).willReturn(Status.NOT_FOUND))

        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.delete(deleteRequest)}
    }

    @Test
    fun `test delete pixKey with incorrect pixId`() {
        val deleteRequest = KeyDeleteRequestDto("06628726061", "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        stubFor(unaryMethod(KeyServiceGrpc.getDeleteMethod()).willReturn(Status.NOT_FOUND))

        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.delete(deleteRequest)}
    }

    @Test
    fun `test delete pixKey without connection to the server`() {
        val deleteRequest = KeyDeleteRequestDto("06628726061", "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        stubFor(unaryMethod(KeyServiceGrpc.getDeleteMethod()).willReturn(Status.UNAVAILABLE))

        val controller = KeyController(gRpcClient, keyService)
        assertThrows(HttpStatusException::class.java) {controller.delete(deleteRequest)}
    }

    @Test
    fun `test retrieve pixKey by pixId and clientId`() {
        // TODO: Terminar implementação dos testes
    }
}