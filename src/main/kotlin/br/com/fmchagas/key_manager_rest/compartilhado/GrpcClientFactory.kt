package br.com.fmchagas.key_manager_rest.compartilhado

import br.com.fmchagas.key_manager_grpc.grpc.NovaChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("key_manager_grpc_client") val canal : ManagedChannel) {
    /*@Singleton
    fun registraChave(
        @GrpcChannel("key_manager_grpc_client") canal : ManagedChannel
    ) : NovaChavePixServiceGrpc.NovaChavePixServiceBlockingStub{
        return NovaChavePixServiceGrpc.newBlockingStub(canal)
    }*/

    @Singleton
    fun registraChave() = NovaChavePixServiceGrpc.newBlockingStub(canal)

}