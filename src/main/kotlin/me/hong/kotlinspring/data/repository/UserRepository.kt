package me.hong.kotlinspring.data.repository

import me.hong.kotlinspring.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
  fun findByEmail(email: String): User?
}