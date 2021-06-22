package br.com.fmchagas.key_manager_rest.chave_pix.cadastra

import br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid
import kotlin.io.path.toPath

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class NovaChavePixController(
    @Inject val grpcClient: NovaChavePixServiceGrpc.NovaChavePixServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post("/chaves")
    fun cadastrar(@PathVariable clienteId: UUID, @Valid request: NovaChavePixRequest) : MutableHttpResponse<Any>{
        logger.info("criando uma nova chave pix para clienteId: $clienteId");

        val response = grpcClient.registrar(request.paraModeloGrpc(clienteId))

        val uri  = UriBuilder
            .of("/api/v1/clientes/{clienteId}/chaves/{pixId}")
            .expand(mutableMapOf("clienteId" to clienteId, "pixId" to response.pixId))

        return HttpResponse.created(uri)
    }
}