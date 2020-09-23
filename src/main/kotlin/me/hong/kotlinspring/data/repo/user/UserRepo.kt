package me.hong.kotlinspring.data.repo.user

import me.hong.kotlinspring.data.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, Long> {
  fun findByEmail(email: String): User?

  fun existsByEmail(email: String): Boolean

  fun existsByName(name: String): Boolean
}