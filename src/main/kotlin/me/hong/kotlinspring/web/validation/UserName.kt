package me.hong.kotlinspring.web.validation

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Documented
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Constraint(validatedBy = [UserNameValidator::class])
annotation class UserName(
    val message: String = "잘못된 이름입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UserNameValidator : ConstraintValidator<UserName, String> {

  override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
    return if (value != null) {
      value.length >= 2
    } else {
      false
    }
  }
}