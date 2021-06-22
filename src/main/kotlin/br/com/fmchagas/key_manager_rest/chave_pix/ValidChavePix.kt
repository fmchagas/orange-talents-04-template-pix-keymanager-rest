package br.com.fmchagas.key_manager_rest.chave_pix

import br.com.fmchagas.key_manager_rest.chave_pix.cadastra.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(TYPE, CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidChavePixValidator::class])
annotation class ValidChavePix(
    val message: String = "Chave pix inválida para o tipo (\${validatedValue.tipoChave})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidChavePixValidator: ConstraintValidator<ValidChavePix, NovaChavePixRequest> {
    override fun isValid(
        value: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value?.tipoChave == null) return false

        return value.tipoChave.valida(value.chaveDoPix)
    }
}