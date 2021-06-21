package br.com.fmchagas.key_manager_rest.chave_pix.cadastra

import br.com.fmchagas.key_manager_grpc.grpc.TipoChave
import br.com.fmchagas.key_manager_grpc.grpc.TipoConta
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeChave
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeConta
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Introspected
data class NovaChavePixRequest(
    @field: NotNull val tipoChave: TipoDeChave,
    @field: Size(max = 77) val chaveDoPix: String?,
    @field: NotNull val tipoConta: TipoDeConta
) {

    override fun toString(): String {
        return "NovaChavePixRequest('tipoChave=$tipoChave, chaveDoPix='$chaveDoPix', tipoConta=$tipoConta)"
    }

    fun paraModeloGrpc(clienteId: UUID): br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixRequest {
        return br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(TipoChave.valueOf(tipoChave.name))
            .setChaveDoPix(chaveDoPix ?: "")
            .setTipoConta(tipoConta.paraTipoGrpc)
            .build()
    }
}
