package me.hong.kotlinspring.data.entity.board

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(indexes = [
  Index(name = "IndexBoardCreatedBy", columnList = "createdBy")
])
@EntityListeners(AuditingEntityListener::class)
class Board(
    title: String,
    content: String
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  @Column(length = 200)
  var title: String = title

  @Column(length = 1000)
  var content: String = content

  var active: Boolean = true

  var likeCount: Long = 0L

  var hateCount: Long = 0L

  var hitCount: Long = 0

  @CreatedBy
  val createdBy: Long? = null

  @CreationTimestamp
  val createdAt: LocalDateTime? = null

  @UpdateTimestamp
  val updatedAt: LocalDateTime? = null

  fun update(board: Board) {
    this.title = board.title
    this.content = board.content
  }

  fun deactivate() {
    this.active = false
  }

  fun hit() {
    this.hitCount++
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