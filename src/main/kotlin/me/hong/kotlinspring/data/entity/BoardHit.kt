package me.hong.kotlinspring.data.entity

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.embedded.BoardHitId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class BoardHit(id: BoardHitId?, likeOrHate: LikeOrHate) {

  @EmbeddedId
  val id: BoardHitId? = id

  @Enumerated(EnumType.STRING)
  @Column(length = 4)
  var likeOrHate: LikeOrHate = likeOrHate

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null
}