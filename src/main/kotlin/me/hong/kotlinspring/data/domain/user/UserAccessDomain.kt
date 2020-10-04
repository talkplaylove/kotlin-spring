package me.hong.kotlinspring.data.domain.user

import me.hong.kotlinspring.data.entity.user.UserAccess
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.data.repo.user.UserAccessRepo
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class UserAccessDomain(
    private val userAccessRepo: UserAccessRepo
) {
  fun getOptional(userId: Long, date: LocalDate): Optional<UserAccess> {
    return userAccessRepo.findById(UserAccessId(userId, date))
  }

  fun create(userId: Long, date: LocalDate): UserAccess {
    val access = UserAccess(UserAccessId(userId, date))
    return userAccessRepo.insert(access)
  }
}