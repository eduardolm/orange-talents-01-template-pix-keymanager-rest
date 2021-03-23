package br.com.zup.service

import br.com.zup.dto.response.AccountReturnDto
import br.com.zup.dto.response.OrganizationResponseDto
import br.com.zup.dto.response.OwnerResponseDto
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

@MicronautTest
class CustomerClientTest {

    private lateinit var accountReturn: Optional<AccountReturnDto>
    private lateinit var mockService: CustomerClient

    @BeforeEach
    fun setup() {
        val institution = OrganizationResponseDto("ITAÚ UNIBANCO S.A.", "60701190")
        val owner = OwnerResponseDto("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "Alterto Tavares",
            "06628726061")

        accountReturn = Optional.of(AccountReturnDto("CONTA_CORRENTE", institution, "0001",
            "212233", owner))

        mockService = Mockito.mock(CustomerClient::class.java)
    }

    @Test
    fun `test findByIdAndAccountType when id and account type exist`() {

        Mockito.`when`(mockService.findByIdAndAccountType(Mockito.anyString(),
            Mockito.anyString()))
            .thenReturn(accountReturn)

        val actual = mockService.findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "CONTA_CORRENTE")

        Mockito.verify(mockService).findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "CONTA_CORRENTE")
    }

    @Test
    fun `test findByIdAndAccountType when id does not exist and account type exists`() {
        Mockito.`when`(mockService.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.empty())

        val actual = mockService.findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "CONTA_CORRENTE")

        Mockito.verify(mockService).findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "CONTA_CORRENTE")
    }

    @Test
    fun `test findByIdAndAccountType when id exists and account type does not exist`() {
        Mockito.`when`(mockService.findByIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.empty())

        val actual = mockService.findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "TIPO_DESCONHECIDO")

        Mockito.verify(mockService).findByIdAndAccountType("0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "TIPO_DESCONHECIDO")
    }
}