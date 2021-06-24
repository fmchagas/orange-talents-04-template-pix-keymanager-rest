package br.com.fmchagas.key_manager_rest.chave_pix.lista

import br.com.fmchagas.key_manager_grpc.grpc.ListaChavePixRequestGrpc
import br.com.fmchagas.key_manager_grpc.grpc.ListaChavePixResponseGrpc
import br.com.fmchagas.key_manager_grpc.grpc.ListarChavePixServiceGrpc
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeChave
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeConta
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject

@Controller("/api/v1/clientes/{clienteId}/chaves")
class ListaChavePixController(@Inject val grpcClient: ListarChavePixServiceGrpc.ListarChavePixServiceBlockingStub) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get
    fun lista(@PathVariable clienteId: UUID) = grpcClient.also {
        logger.info("Listando chave pix para clienteId: $clienteId")
    }.lista(
        ListaChavePixRequestGrpc.newBuilder().setClientId(clienteId.toString()).build()
    ).let {chavesGrpc ->
        ListaChavePixResponse.converte(chavesGrpc).let {
            HttpResponse.ok(it)
        }
    }
}

@Introspected
data class ListaChavePixResponse(
    val pixId: String,
    val tipoChave: TipoDeChave,
    val chavePix: String,
    val tipoConta: TipoDeConta,
    val criadoEm: String
) {
    companion object {
        fun converte(chavePix2: ListaChavePixResponseGrpc): List<ListaChavePixResponse> {
            return chavePix2.chavesList.map {
                ListaChavePixResponse(
                    it.pixId,
                    TipoDeChave.valueOf(it.tipoChave.name),
                    it.chavePix,
                    TipoDeConta.valueOf(it.tipoConta.name),
                    it.criadoEm.let { timestamp ->
                        /*LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong()), TimeZone.getDefault().toZoneId()).let { dateTime ->
                            SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(dateTime.toString())
                        }*/
                        LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong()),
                            TimeZone.getDefault().toZoneId()
                        ).toString()
                    }
                )
            }
        }
    }
}