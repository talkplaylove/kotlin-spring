package me.hong.kotlinspring.data.entity.board

import me.hong.kotlinspring.data.enums.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.embedded.BoardCommentReadId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(indexes = [
  Index(name = "IndexBoardCommentReadUserId", columnList = "userId", unique = false)
])
class BoardCommentRead(id: BoardCommentReadId, likeOrHate: LikeOrHate) {

  @EmbeddedId
  val id: BoardCommentReadId = id

  @Enumerated(EnumType.STRING)
  @Column(length = 4)
  var likeOrHate: LikeOrHate = likeOrHate

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null

  fun read(likeOrHate: LikeOrHate) {
    this.likeOrHate = likeOrHate
  }
}