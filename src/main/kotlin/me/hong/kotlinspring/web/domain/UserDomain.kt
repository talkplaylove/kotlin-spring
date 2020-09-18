package me.hong.kotlinspring.web.domain

import me.hong.kotlinspring.data.entity.user.User
import me.hong.kotlinspring.data.entity.user.UserAccess
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.data.repo.user.UserAccessRepo
import me.hong.kotlinspring.data.repo.user.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.stereotype.Component

@Component
class UserDomain(
    private val userRepo: UserRepo,
    private val userAccessRepo: UserAccessRepo
) {
  fun getUser(id: Long?): User? {
    return userRepo.findById(id)
  }

  fun getUsers(ids: Set<Long?>): MutableList<User> {
    return userRepo.findAllById(ids)
  }

  fun findUser(email: String): User {
    return userRepo.findByEmail(email)
        ?: throw CustomException(CustomMessage.USER_NOT_FOUND)
  }

  fun hitUserAccess(accessId: UserAccessId) {
    userAccessRepo.findById(accessId).ifPresentOrElse({
      it.hitCount++
    }, {
      userAccessRepo.insert(UserAccess(accessId))
    })
  }

  fun createUser(user: User): User {
    return userRepo.save(user)
  }

  fun existsEmail(email: String): Boolean {
    return userRepo.existsByEmail(email)
  }

  fun existsName(name: String): Boolean {
    return userRepo.existsByName(name)
  }
}