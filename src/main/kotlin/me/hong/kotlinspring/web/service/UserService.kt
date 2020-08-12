package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.repo.user.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.model.user.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private val passwordEncoder: PasswordEncoder
) {
  fun signin(req: SigninReq): SigninRes {
    val user = userRepo.findByEmail(req.email)
        ?: throw CustomException(CustomMessage.USER_NOT_FOUND)

    if (!passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.INCORRECT_PASSWORD)

    return SigninRes.of(user)
  }

  fun signup(req: SignupReq): SignupRes {
    val encodedPassword = passwordEncoder.encode(req.password)
    val user = userRepo.save(req.toEntity(encodedPassword))

    return SignupRes.of(user)
  }

  fun duplicateEmail(email: String): UserDuplicateRes {
    val duplicated = userRepo.existsByEmail(email)

    return UserDuplicateRes.of(duplicated)
  }

  fun duplicateName(name: String): UserDuplicateRes {
    val duplicated = userRepo.existsByName(name)

    return UserDuplicateRes.of(duplicated)
  }
}