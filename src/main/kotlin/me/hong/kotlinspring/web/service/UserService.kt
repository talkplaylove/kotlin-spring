package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.domain.user.UserAccessDomain
import me.hong.kotlinspring.data.domain.user.UserDomain
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.model.user.SigninReq
import me.hong.kotlinspring.web.model.user.SigninRes
import me.hong.kotlinspring.web.model.user.SignupReq
import me.hong.kotlinspring.web.model.user.SignupRes
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class UserService(
    private val userDomain: UserDomain,
    private val userAccessDomain: UserAccessDomain,
    private val boardService: BoardService,
    private val passwordEncoder: PasswordEncoder
) {
  @Transactional
  fun signin(req: SigninReq): SigninRes {
    val user = userDomain.getOne(req.email)

    if (!passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.INCORRECT_PASSWORD)

    userAccessDomain.access(UserAccessId(user.id!!, LocalDate.now()))

    return SigninRes.of(user)
  }

  @Transactional
  fun signup(req: SignupReq): SignupRes {
    this.duplicateEmail(req.email)
    this.duplicateName(req.name)

    val encodedPassword = passwordEncoder.encode(req.password)
    val user = userDomain.create(req.toUser(encodedPassword))

    boardService.createBoardUser(user.id!!, user.name)
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
}