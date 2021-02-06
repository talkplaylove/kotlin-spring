package me.hong.kotlinspring.data.entity.board.embedded

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class BoardHitId(boardId: Long?, date: LocalDate?, ip: String?) : Serializable {
  @Column(name = "boardId")
  var id1: Long? = boardId

  @Column(name = "date")
  var id2: LocalDate? = date

  @Column(name = "ip", length = 50)
  var id3: String? = ip

}