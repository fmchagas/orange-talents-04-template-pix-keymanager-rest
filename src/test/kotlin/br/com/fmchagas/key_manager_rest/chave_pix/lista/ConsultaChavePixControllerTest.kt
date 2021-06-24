package br.com.fmchagas.key_manager_rest.chave_pix.lista

import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixServiceGrpc
import br.com.fmchagas.key_manager_rest.compartilhado.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaChavePixControllerTest {
    @field:Inject
    lateinit var clientGrpc: ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve cnsultar uma nova chave pix quando dados forem validos`() {

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMockConsulta() = Mockito.mock(ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub::class.java)
    }
}