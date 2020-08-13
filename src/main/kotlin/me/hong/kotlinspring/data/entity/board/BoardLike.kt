package me.hong.kotlinspring.data.entity.board

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.embedded.BoardLikeId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(indexes = [
  Index(name = "IndexBoardLikeUserId", columnList = "userId", unique = false)
])
class BoardLike(id: BoardLikeId?, likeOrHate: LikeOrHate) {

  @EmbeddedId
  val id: BoardLikeId? = id

  @Enumerated(EnumType.STRING)
  @Column(length = 4)
  var likeOrHate: LikeOrHate = likeOrHate

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null
}