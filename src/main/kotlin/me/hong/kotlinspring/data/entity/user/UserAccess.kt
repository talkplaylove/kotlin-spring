package me.hong.kotlinspring.data.entity.user

import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
class UserAccess(id: UserAccessId) {

  @EmbeddedId
  val id: UserAccessId = id

  var hit: Long = 1

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null
}