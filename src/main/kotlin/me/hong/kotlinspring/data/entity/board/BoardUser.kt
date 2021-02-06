package me.hong.kotlinspring.data.entity.board

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class BoardUser(
  userId: Long,
  userName: String
) {
  @Id
  val userId: Long = userId

  @Column(length = 100)
  var userName: String = userName

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null

  fun update(userName: String) {
    this.userName = userName
  }
}