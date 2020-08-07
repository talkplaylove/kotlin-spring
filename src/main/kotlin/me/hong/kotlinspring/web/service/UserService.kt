package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.repository.UserRepository
import me.hong.kotlinspring.web.advice.ErrorMessage
import me.hong.kotlinspring.web.advice.LogicException
import me.hong.kotlinspring.web.model.SigninReq
import me.hong.kotlinspring.web.model.SigninRes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

  @Autowired
  lateinit var userRepository: UserRepository

  fun signin(req: SigninReq): SigninRes {
    val user = userRepository.findByEmail(req.email)
    return if (user != null) {
      SigninRes(user.id, user.name)
    } else {
      throw LogicException(ErrorMessage.USER_NOT_FOUND)
    }
  }

}