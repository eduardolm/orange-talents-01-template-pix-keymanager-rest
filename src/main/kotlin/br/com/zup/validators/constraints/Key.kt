package br.com.zup.validators.constraints

import br.com.zup.validators.KeyValidator
import javax.validation.Constraint
import javax.validation.ReportAsSingleViolation

@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.CLASS)
@ReportAsSingleViolation
@MustBeDocumented
@Constraint(validatedBy = [KeyValidator::class])
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Key(val message:String = "Chave inválida.")
