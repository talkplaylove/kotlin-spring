package me.hong.kotlinspring.data.domain.user

import me.hong.kotlinspring.data.entity.user.User
import me.hong.kotlinspring.data.repo.user.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDomain(
    private val userRepo: UserRepo
) {
  fun getOptional(email: String): Optional<User> {
    return userRepo.findByEmail(email)
  }

  fun getOne(email: String): User {
    return this.getOptional(email).orElseThrow {
      throw CustomException(CustomMessage.USER_NOT_FOUND)
    }
  }

  fun create(user: User): User {
    return userRepo.save(user)
  }

  fun existsEmail(email: String): Boolean {
    return userRepo.existsByEmail(email)
  }

  fun existsName(name: String): Boolean {
    return userRepo.existsByName(name)
  }
}