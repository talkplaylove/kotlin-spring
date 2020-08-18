package me.hong.kotlinspring.data.entity.board.embedded

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class BoardReadId(boardId: Long?, userId: Long?) : Serializable {
  @Column(name = "boardId")
  var id1: Long? = boardId

  @Column(name = "userId")
  var id2: Long? = userId

  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}