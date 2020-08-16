package me.hong.kotlinspring.data.entity.board

import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
class BoardHit(id: BoardHitId) {

  @EmbeddedId
  val id: BoardHitId = id
}