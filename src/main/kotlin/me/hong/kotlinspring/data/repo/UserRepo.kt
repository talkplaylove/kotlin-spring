package me.hong.kotlinspring.data.repo

import me.hong.kotlinspring.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, Long> {
  fun findByEmail(email: String): User?
}