package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.user.User
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.domain.UserDomain
import me.hong.kotlinspring.web.model.user.SigninReq
import me.hong.kotlinspring.web.model.user.SigninRes
import me.hong.kotlinspring.web.model.user.SignupReq
import me.hong.kotlinspring.web.model.user.SignupRes
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class UserService(
    private val userDomain: UserDomain,
    private val passwordEncoder: PasswordEncoder
) {
  @Transactional
  fun signin(req: SigninReq): SigninRes {
    val user = userDomain.findUser(req.email)

    if (!passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.INCORRECT_PASSWORD)

    userDomain.hitUserAccess(UserAccessId(user.id!!, LocalDate.now()))

    return SigninRes.of(user)
  }

  fun signup(req: SignupReq): SignupRes {
    this.duplicateEmail(req.email)
    this.duplicateName(req.name)

    val encodedPassword = passwordEncoder.encode(req.password)
    val user = userDomain.createUser(req.toEntity(encodedPassword))

    return SignupRes.of(user)
  }

  fun duplicateEmail(email: String) {
    if (userDomain.existsEmail(email))
      throw CustomException(CustomMessage.EXISTS_EMAIL)
  }

  fun duplicateName(name: String) {
    if (userDomain.existsName(name))
      throw CustomException(CustomMessage.EXISTS_NAME)
  }

  fun getUser(id: Long?): User? {
    return userDomain.getUser(id)
  }

  fun getUsers(ids: Set<Long?>): Map<Long?, User> {
    val users = userDomain.getUsers(ids)
    return users.stream().collect(Collectors.toMap(User::id) { it }).toMap()
  }
}