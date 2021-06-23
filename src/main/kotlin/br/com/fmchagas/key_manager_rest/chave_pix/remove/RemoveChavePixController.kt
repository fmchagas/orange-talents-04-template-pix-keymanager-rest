package br.com.fmchagas.key_manager_rest.chave_pix.remove

import br.com.fmchagas.key_manager_grpc.grpc.RemoveChavePixRequestGrpc
import br.com.fmchagas.key_manager_grpc.grpc.RemoveChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Validated
@Controller("/api/v1/clientes/{clienteId}/chaves")
class RemoveChavePixController(
    @Inject val grpcClient:  RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete
    fun remove(@PathVariable clienteId: UUID, pixId: UUID) : HttpResponse<Any>{
        logger.info("removendo nova chave pix para clienteId: $clienteId e pixId: $pixId")

        val response = grpcClient.remover(
            RemoveChavePixRequestGrpc.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )

        return HttpResponse.ok(RemoveChavePixResponse(response.mensagem))
    }

    data class RemoveChavePixResponse(val message: String)
}