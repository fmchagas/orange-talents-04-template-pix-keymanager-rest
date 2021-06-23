package br.com.fmchagas.key_manager_rest.chave_pix.remove

import br.com.fmchagas.key_manager_grpc.grpc.RemoveChavePixResponseGrpc
import br.com.fmchagas.key_manager_grpc.grpc.RemoveChavePixServiceGrpc
import br.com.fmchagas.key_manager_rest.compartilhado.GrpcClientFactory
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
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest{

    @field:Inject
    lateinit var clientGrpc: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve remover uma nova chave pix quando dados forem validos`() {

        val pixId = "49e36fff-8fae-4bc8-b8a6-e804bcc0b45c"
        val clienteId = "c56dfef4-7901-44fb-84e2-a2cefb157890"

        val responseGrpc = RemoveChavePixResponseGrpc.newBuilder()
            .setMensagem("Removido com sucesso")
            .build()

        Mockito.`when`(clientGrpc.remover(Mockito.any())).thenReturn(responseGrpc)

        val request = HttpRequest.DELETE("/api/v1/clientes/$clienteId/chaves", "{\"pixId\":\"$pixId\"}")
        val response = client.toBlocking().exchange(request, pixId::class.java)

        with(response){
            assertEquals(HttpStatus.OK, status)
            assertTrue(response.body()?.contains("Removido com sucesso") == true)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMockRemove() = Mockito.mock(RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub::class.java)
    }
}