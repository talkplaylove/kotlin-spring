package me.hong.kotlinspring.data.entity.board

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
  indexes = [
    Index(name = "IndexBoardCommentBoardId", columnList = "boardId"),
    Index(name = "IndexBoardCommentCreatedBy", columnList = "createdBy")
  ]
)
@EntityListeners(AuditingEntityListener::class)
class BoardComment(
  boardId: Long,
  content: String
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  val boardId: Long = boardId

  @Column(length = 500)
  var content: String = content

  var active: Boolean = true

  var likeCount: Long = 0L

  var hateCount: Long = 0L

  @CreatedBy
  var createdBy: Long? = null

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null

  fun update(comment: BoardComment) {
    this.content = comment.content
  }

  fun deactivate() {
    this.active = false
  }

  fun like() {
    this.likeCount++
  }

  fun unlike() {
    this.likeCount--
  }

  fun hate() {
    this.hateCount++
  }

  fun unhate() {
    this.hateCount--
  }

  fun unhateAndLike() {
    this.unhate()
    this.like()
  }

  fun unlikeAndHate() {
    this.unlike()
    this.hate()
  }
}