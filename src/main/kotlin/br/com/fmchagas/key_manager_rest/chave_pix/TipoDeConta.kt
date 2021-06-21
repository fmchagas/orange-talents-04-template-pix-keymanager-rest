package br.com.fmchagas.key_manager_rest.chave_pix

import br.com.fmchagas.key_manager_grpc.grpc.TipoConta

enum class TipoDeConta(val paraTipoGrpc: TipoConta) {
    POUPANCA(TipoConta.POUPANCA),
    CORRENTE(TipoConta.CORRENTE)
}