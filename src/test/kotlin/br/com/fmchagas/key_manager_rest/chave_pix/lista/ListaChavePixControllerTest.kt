package br.com.fmchagas.key_manager_rest.chave_pix.lista

import br.com.fmchagas.key_manager_grpc.grpc.*
import br.com.fmchagas.key_manager_rest.compartilhado.GrpcClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
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
internal class ListaChavePixControllerTest {
    @field:Inject
    lateinit var clientGrpc: ListarChavePixServiceGrpc.ListarChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    companion object {
        val CLIENTE_ID = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val CHAVE_EMAIL = "fernando@gmail.com"
        val CHAVE_CPF = "73007268010"
        val CONTA_CORRENTE = TipoConta.CORRENTE
        val TIPO_DE_CHAVE_EMAIL = TipoChave.EMAIL
        val TIPO_DE_CHAVE_CPF = TipoChave.CPF
    }

    @Test
    fun `deve retornar lista de todas chaves pix quando existirem`() {
        // cenário
        val responseGrpc = criaListaChavePixResponseGrpc()

        Mockito.`when`(clientGrpc.lista(Mockito.any())).thenReturn(responseGrpc)

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/chaves")
        val response: HttpResponse<List<ListaChavePixResponse>> = client.toBlocking()
            .exchange(request, Argument.listOf(ListaChavePixResponse::class.java))

        // asserção
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

        response.body()?.let {
            assertEquals(2, it.size)
            assertEquals(CHAVE_CPF, it[0].chavePix)
            assertEquals(CHAVE_EMAIL, it[1].chavePix)
            assertEquals(TIPO_DE_CHAVE_CPF.name, it[0].tipoChave.name)
            assertEquals(TIPO_DE_CHAVE_EMAIL.name, it[1].tipoChave.name)
        }
    }

    @Test
    fun `deve retornar lista vazia quando chaves pix nao existirem`() {
        // cenário
        Mockito.`when`(clientGrpc.lista(Mockito.any())).thenReturn(
            ListaChavePixResponseGrpc.newBuilder()
                .setClientId(CLIENTE_ID)
                .build()
        )

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/chaves")
        val response: HttpResponse<List<ListaChavePixResponse>> = client.toBlocking()
            .exchange(request, Argument.listOf(ListaChavePixResponse::class.java))

        // asserção
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(0, response.body().size)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMockLista() = Mockito.mock(ListarChavePixServiceGrpc.ListarChavePixServiceBlockingStub::class.java)
    }

    fun criaListaChavePixResponseGrpc(): ListaChavePixResponseGrpc {

        val chaveComCpf = ListaChavePixResponseGrpc.ListaChavePixGrpc.newBuilder()
            .setChavePix(CHAVE_CPF)
            .setPixId(UUID.randomUUID().toString())
            .setTipoChave(TIPO_DE_CHAVE_CPF)
            .setTipoConta(CONTA_CORRENTE)
            .setCriadoEm(
                Timestamp.newBuilder()
                    .setSeconds(1624484417)
                    .setNanos(342775000)
                    .build()
            )
            .build()

        val chaveComEmail = ListaChavePixResponseGrpc.ListaChavePixGrpc.newBuilder()
            .setChavePix(CHAVE_EMAIL)
            .setPixId(UUID.randomUUID().toString())
            .setTipoChave(TIPO_DE_CHAVE_EMAIL)
            .setTipoConta(CONTA_CORRENTE)
            .setCriadoEm(
                Timestamp.newBuilder()
                    .setSeconds(1624484417)
                    .setNanos(342775000)
                    .build()
            )
            .build()


        return ListaChavePixResponseGrpc.newBuilder()
            .setClientId(CLIENTE_ID)
            .addAllChaves(listOf(chaveComCpf, chaveComEmail))
            .build()
    }
}