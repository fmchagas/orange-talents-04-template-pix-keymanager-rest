package br.com.fmchagas.key_manager_rest.chave_pix.lista

import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixRequestGrpc
import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixResponseGrpc
import br.com.fmchagas.key_manager_grpc.grpc.ConsultarChavePixServiceGrpc
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeChave
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/{clienteId}/chaves/{pixId}")
class ConsultaChavePixController(
    @Inject val grpcClient: ConsultarChavePixServiceGrpc.ConsultarChavePixServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get
    fun consuta(@PathVariable clienteId: UUID, pixId: UUID) = grpcClient.also {
        logger.info("Consulta uma chave pix para clienteId: $clienteId e pixId: $pixId")
    }.consulta(
        ConsultarChavePixRequestGrpc.newBuilder()
            .setPixId(
                ConsultarChavePixRequestGrpc.FiltroPorPixId.newBuilder()
                    .setClienteId(clienteId.toString())
                    .setPixId(pixId.toString())
                    .build()
            )
            .build()
    ).let {
        ConsultaChavePixResponse(it.chave.chavePix,
            TipoDeChave.valueOf(it.chave.tipo.name),
            mapOf(
                Pair("tipo", TipoDeConta.valueOf(it.chave.conta.tipo.name).name),
                Pair("instituicao", it.chave.conta.instituicao),
                Pair("titular", it.chave.conta.titular),
                Pair("CPF", it.chave.conta.cpf),
                Pair("agencia", it.chave.conta.agencia),
                Pair("conta", it.chave.conta.conta)
            ),
            it.chave.criadoEm.let { timestamp ->
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong()),
                    ZoneOffset.UTC
                )
            }
        )
    }.let {
        HttpResponse.ok(it)
    }
}


data class ConsultaChavePixResponse(
    val chavePix: String,
    val tipoChave: TipoDeChave,
    val conta: Map<String, String>,
    val criadoEm: LocalDateTime
)