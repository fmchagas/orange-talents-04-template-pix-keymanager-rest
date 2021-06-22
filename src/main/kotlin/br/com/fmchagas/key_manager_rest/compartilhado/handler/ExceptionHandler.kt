package br.com.fmchagas.key_manager_rest.compartilhado.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<Any> {
        val descricao = exception.status.description ?: ""
        val statusCode = exception.status.code

        when (statusCode) {
            //juntar INVALID_ARGUMENT e FAILED_PRECONDITION
            Status.Code.INVALID_ARGUMENT, Status.Code.FAILED_PRECONDITION -> {
                return HttpResponse
                    .status<JsonError>(HttpStatus.BAD_REQUEST)
                    .body(JsonError(descricao))
            }
            //Status.Code.FAILED_PRECONDITION -> HttpStatusException(HttpStatus.BAD_REQUEST, descricao)

            Status.Code.ALREADY_EXISTS -> {
                return HttpResponse
                    .status<JsonError>(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(JsonError(descricao))
            }

            Status.Code.NOT_FOUND -> {
                return HttpResponse
                    .status<JsonError>(HttpStatus.NOT_FOUND)
                    .body(JsonError(descricao))
            }
        }

        logger.error("Ops, Erro '${exception.javaClass.name}' ao processar a chamada ", exception)
        return HttpResponse
            .status<JsonError>(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(JsonError("Nao foi possivel completar a requisição devido ao erro: [$descricao - $statusCode]"))
    }
}