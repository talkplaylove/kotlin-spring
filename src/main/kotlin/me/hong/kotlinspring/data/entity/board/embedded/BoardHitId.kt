package me.hong.kotlinspring.data.entity.board.embedded

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
class BoardHitId(boardId: Long?, userId: Long?) : Serializable {
  var boardId: Long? = boardId
  var userId: Long? = userId

  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }
}