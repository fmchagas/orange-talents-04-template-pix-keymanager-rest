package br.com.fmchagas.key_manager_rest.compartilhado.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExceptionHandlerTest{
    private val request = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 400 quando exception do gRPC for INVALID_ARGUMENT`() {
        val mensagem = "Dados estão inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        val resultado = ExceptionHandler().handle(request, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, resultado.status)
        assertNotNull(resultado.body())
        assertEquals(mensagem, (resultado.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 400 quando exception do gRPC for FAILED_PRECONDITION`() {
        val mensagem = "não foi possivel criar chave pix no banco central"
        val failedPreconditionException = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription(mensagem))

        val resultado = ExceptionHandler().handle(request, failedPreconditionException)

        assertEquals(HttpStatus.BAD_REQUEST, resultado.status)
        assertNotNull(resultado.body())
        assertEquals(mensagem, (resultado.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 422 quando exception do gRPC for ALREADY_EXISTS`() {

        val mensagem = "chave pix 'xpto' já cadastrada no sistema"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS
            .withDescription(mensagem))

        val resultado = ExceptionHandler().handle(request, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resultado.status)
        assertNotNull(resultado.body())
        assertEquals(mensagem, (resultado.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 404 quando exception do gRPC for NOT_FOUND`() {

        val mensagem = "Conta do cliente não encontrada"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND
            .withDescription(mensagem))

        val resultado = ExceptionHandler().handle(request, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, resultado.status)
        assertNotNull(resultado.body())
        assertEquals(mensagem, (resultado.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 500 quando qualquer outro erro for INTERNAL`() {

        val internalException = StatusRuntimeException(Status.INTERNAL)

        val resposta = ExceptionHandler().handle(request, internalException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
        assertNotNull(resposta.body())
        assertTrue((resposta.body() as JsonError).message.contains("INTERNAL"))
    }

}