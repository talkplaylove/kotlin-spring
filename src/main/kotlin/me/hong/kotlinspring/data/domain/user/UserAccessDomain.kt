package me.hong.kotlinspring.data.domain.user

import me.hong.kotlinspring.data.entity.user.UserAccess
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.data.repo.user.UserAccessRepo
import org.springframework.stereotype.Component

@Component
class UserAccessDomain(
    private val userAccessRepo: UserAccessRepo
) {
  fun access(accessId: UserAccessId) {
    userAccessRepo.findById(accessId).ifPresentOrElse({
      it.hitCount++
    }, {
      userAccessRepo.insert(UserAccess(accessId))
    })
  }
}