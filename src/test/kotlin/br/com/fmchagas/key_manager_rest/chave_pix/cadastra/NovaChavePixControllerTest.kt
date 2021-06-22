package br.com.fmchagas.key_manager_rest.chave_pix.cadastra


import br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixResponse
import br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixServiceGrpc
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeChave
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeConta
import br.com.fmchagas.key_manager_rest.compartilhado.GrpcClientFactory
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpPost
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class NovaChavePixControllerTest {
    @field:Inject
    lateinit var clientGrpc: NovaChavePixServiceGrpc.NovaChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registrar uma nova chave pix quando dados forem validos`() {
        val pixId = "49e36fff-8fae-4bc8-b8a6-e804bcc0b45c"
        val clienteId = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val responseGrpc = NovaChavePixResponse.newBuilder().setPixId(pixId).build()

        Mockito.`when`(clientGrpc.registrar(Mockito.any())).thenReturn(responseGrpc)

        val novaChavePix = NovaChavePixRequest(
            TipoDeChave.CHAVE_ALEATORIA,
            "",
            TipoDeConta.CORRENTE)

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/chaves", novaChavePix)
        val response = client.toBlocking().exchange(request, NovaChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location").contains(pixId))
        assertTrue(response.header("Location").contains(clienteId))
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(NovaChavePixServiceGrpc.NovaChavePixServiceBlockingStub::class.java)
    }
}