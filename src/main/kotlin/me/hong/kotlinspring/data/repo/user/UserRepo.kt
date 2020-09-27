package me.hong.kotlinspring.data.repo.user

import me.hong.kotlinspring.data.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo : JpaRepository<User, Long> {
  fun findByEmail(email: String): Optional<User>

  fun existsByEmail(email: String): Boolean

  fun existsByName(name: String): Boolean
}