package me.hong.kotlinspring.web.service.user

import me.hong.kotlinspring.data.domain.user.UserAccessDomain
import me.hong.kotlinspring.data.domain.user.UserDomain
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.SigninUser
import me.hong.kotlinspring.web.model.user.*
import me.hong.kotlinspring.web.service.board.BoardService
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

    val userId = user.id!!
    val date = LocalDate.now()
    userAccessDomain.getOptional(userId, date).ifPresentOrElse({
      it.hit()
    }, {
      userAccessDomain.create(userId, date)
    })

    return SigninRes.of(user)
  }

  @Transactional
  fun signup(req: SignupReq): SignupRes {
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

  @Transactional
  fun updateEmail(req: UserEmailPutReq, signinUser: SigninUser) {
    val user = userDomain.getOne(signinUser.id)
    if (user.email == req.email)
      throw CustomException(CustomMessage.SAME_VALUES)

    user.updateEmail(req.email)
  }

  @Transactional
  fun updateName(req: UserNamePutReq, signinUser: SigninUser) {
    val user = userDomain.getOne(signinUser.id)
    if (user.name == req.name)
      throw CustomException(CustomMessage.SAME_VALUES)

    user.updateName(req.name)

    boardService.updateBoardUser(signinUser.id, req.name)
  }

  @Transactional
  fun updatePassword(req: UserPasswordPutReq, signinUser: SigninUser) {
    val user = userDomain.getOne(signinUser.id)
    if (passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.SAME_VALUES)

    user.updatePassword(passwordEncoder.encode(req.password))
  }
}