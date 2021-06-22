package br.com.fmchagas.key_manager_rest.chave_pix.cadastra

import br.com.fmchagas.key_manager_grpc.grpc.TipoChave
import br.com.fmchagas.key_manager_grpc.grpc.TipoConta
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeChave
import br.com.fmchagas.key_manager_rest.chave_pix.TipoDeConta
import br.com.fmchagas.key_manager_rest.chave_pix.ValidChavePix
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.util.ArgumentUtils
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidChavePix
@Introspected
data class NovaChavePixRequest(
    @field: NotNull val tipoChave: TipoDeChave,
    @field: Size(max = 77) val chaveDoPix: String?,
    @field: NotNull val tipoConta: TipoDeConta
) {

    fun paraModeloGrpc(clienteId: UUID): br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixRequest {
        ArgumentUtils.requireNonNull("clienteId", clienteId);

        return br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(tipoChave.paraTipoChaveGrpc)
            .setChaveDoPix(chaveDoPix ?: "")
            .setTipoConta(tipoConta.paraTipoGrpc)
            .build()
    }
}
