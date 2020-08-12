package me.hong.kotlinspring.data.entity.user

import me.hong.kotlinspring.constant.user.Gender
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(indexes = [
  Index(name = "IndexUserEmail", columnList = "email", unique = true),
  Index(name = "IndexUserName", columnList = "name", unique = true)
])
class User(
    email: String,
    name: String,
    password: String,
    gender: Gender
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  @Column(length = 320)
  var email: String = email

  @Column(length = 100)
  var name: String = name

  @Column(length = 1000)
  var password: String = password

  @Enumerated(EnumType.STRING)
  @Column(length = 6)
  var gender: Gender = gender

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null
}