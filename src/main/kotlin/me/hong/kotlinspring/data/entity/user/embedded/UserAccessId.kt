package me.hong.kotlinspring.data.entity.user.embedded

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class UserAccessId(userId: Long, date: LocalDate) : Serializable {
  @Column(name = "userId")
  var id1: Long = userId

  @Column(name = "date")
  var id2: LocalDate? = date

  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}