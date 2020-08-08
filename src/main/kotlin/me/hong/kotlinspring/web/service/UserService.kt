package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.data.entity.User
import me.hong.kotlinspring.data.repo.UserRepo
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.model.user.SigninReq
import me.hong.kotlinspring.web.model.user.SigninRes
import me.hong.kotlinspring.web.model.user.SignupReq
import me.hong.kotlinspring.web.model.user.SignupRes
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder,
    private val userSession: UserSession
) {
  fun signin(req: SigninReq): SigninRes {
    val user = userRepo.findByEmail(req.email)
        ?: throw CustomException(CustomMessage.USER_NOT_FOUND)

    if (!passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.INCORRECT_PASSWORD)

    userSession.id = user.id!!
    userSession.name = user.name
    return SigninRes(user.id, user.name, user.gender)
  }

  fun signup(req: SignupReq): SignupRes {
    val user = userRepo.save(User(
        req.email,
        req.name,
        passwordEncoder.encode(req.password),
        req.gender
    ))
    return SignupRes(user.id)
  }
}