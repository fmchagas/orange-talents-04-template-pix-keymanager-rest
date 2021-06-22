package br.com.fmchagas.key_manager_rest.chave_pix

import br.com.fmchagas.key_manager_grpc.grpc.TipoChave
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator


enum class TipoDeChave(val paraTipoChaveGrpc: TipoChave) {
    CPF(TipoChave.CPF) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrEmpty()){
                return false
            }

            return CPFValidator().run {
                initialize(null)
                isValid(chavePix, null)
            }
        }
    },
    TEL_CELULAR(TipoChave.TEL_CELULAR) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrEmpty()){
                return false
            }

            return chavePix.matches("^\\+[1-9][0-9]\\d{1,14}$".toRegex())
        }
    },
    EMAIL(TipoChave.EMAIL) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrEmpty()){
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chavePix, null)
            }
        }
    },
    CHAVE_ALEATORIA(TipoChave.CHAVE_ALEATORIA) {
        override fun valida(chavePix: String?): Boolean {
            return chavePix.isNullOrEmpty() //chavePix deve ser vazia
        }
    };

    abstract fun valida(chavePix: String?): Boolean
}