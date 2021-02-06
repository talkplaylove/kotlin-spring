package me.hong.kotlinspring.web.validation

import java.lang.annotation.Documented
import java.util.regex.Pattern
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Documented
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])
annotation class Password(
  val message: String = "비밀번호는 8~20자의 숫자,문자,특수문자를 포함해야 합니다.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

class PasswordValidator : ConstraintValidator<Password, String> {

  override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
    return Pattern.matches("^(?=.*\\d)(?=.*[~`!@#\$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}\$", value!!)
  }
}