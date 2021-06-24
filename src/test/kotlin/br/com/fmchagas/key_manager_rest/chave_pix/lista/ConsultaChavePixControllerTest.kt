package br.com.fmchagas.key_manager_rest.chave_pix.lista

import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixResponseGrpc
import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixServiceGrpc
import br.com.fmchagas.key_manager_grpc.grpc.TipoChave
import br.com.fmchagas.key_manager_grpc.grpc.TipoConta
import br.com.fmchagas.key_manager_rest.compartilhado.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaChavePixControllerTest(
    @Inject val clientGrpc: ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub
) {
    //@field:Inject
    //lateinit var clientGrpc: ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    companion object {
        const val CLIENTE_ID = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        const val PIX_ID = "205c609c-58e6-4d07-a470-293391d0f96a"
        const val CHAVE_EMAIL = "fernando@gmail.com"
        val CONTA_CORRENTE = TipoConta.CORRENTE
        val TIPO_DE_CHAVE_EMAIL = TipoChave.EMAIL
        const val INSTITUICAO = "UNIBANCO ITAU"
        const val TITULAR = "Fernando"
        const val DOCUMENTO_DO_TITULAR = "73007268010"
        const val AGENCIA = "1218"
        const val NUMERO_DA_CONTA = "291900"
    }

    @Test
    fun `deve cnsultar uma chave pix quando filtro for valido e existir`() {
        // cenário
        Mockito.`when`(clientGrpc.consulta(Mockito.any())).thenReturn(consultarChavePixResponse())

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/chaves/$PIX_ID")
        val response = client.toBlocking().exchange(request, ConsultaChavePixResponse::class.java)

        // asserção
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body()!!)

        with(response.body()!!){
            assertEquals(TIPO_DE_CHAVE_EMAIL.name, this.tipoChave.name)
            assertEquals(CHAVE_EMAIL, this.chavePix)
            assertEquals(CONTA_CORRENTE.name, this.conta["tipo"])
            assertEquals(TITULAR, this.conta["titular"])
            assertEquals(DOCUMENTO_DO_TITULAR, this.conta["CPF"])
            assertEquals(INSTITUICAO, this.conta["instituicao"])
            assertEquals(AGENCIA, this.conta["agencia"])
            assertEquals(NUMERO_DA_CONTA, this.conta["conta"])
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMockConsulta(): ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub =
            Mockito.mock(ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub::class.java)
    }

    private fun consultarChavePixResponse() =
        ConsultarChavePixResponseGrpc.newBuilder()
            .setClienteId(CLIENTE_ID)
            .setPixId(PIX_ID)
            .setChave(
                ConsultarChavePixResponseGrpc.ChavePixGrpc
                    .newBuilder()
                    .setTipo(TIPO_DE_CHAVE_EMAIL)
                    .setChavePix(CHAVE_EMAIL)
                    .setConta(
                        ConsultarChavePixResponseGrpc.ChavePixGrpc.ContaGrpc.newBuilder()
                            .setTipo(CONTA_CORRENTE)
                            .setInstituicao(INSTITUICAO)
                            .setTitular(TITULAR)
                            .setCpf(DOCUMENTO_DO_TITULAR)
                            .setAgencia(AGENCIA)
                            .setConta(NUMERO_DA_CONTA)
                            .build()
                    )
                    .setCriadoEm(
                        Timestamp.newBuilder()
                            .setSeconds(1624484417)
                            .setNanos(342775000)
                            .build()
                    )
            ).build()
}