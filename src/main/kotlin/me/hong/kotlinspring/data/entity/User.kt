package me.hong.kotlinspring.data.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
    @Id
    @GeneratedValue
    val id: Long,

    @Column(length = 320)
    var email: String,

    @Column(length = 100)
    var name: String,

    @Column(length = 1000)
    var password: String,

    @CreationTimestamp
    val createAt: LocalDateTime,

    @UpdateTimestamp
    var updateAt: LocalDateTime
)