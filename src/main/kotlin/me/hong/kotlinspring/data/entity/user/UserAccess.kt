package me.hong.kotlinspring.data.entity.user

import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(indexes = [
  Index(name = "IndexUserAccessCreatedAt", columnList = "createdAt")
])
class UserAccess(id: UserAccessId) {

  @EmbeddedId
  val id: UserAccessId = id

  var hitCount: Long = 1

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null
}