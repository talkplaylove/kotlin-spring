package me.hong.kotlinspring.data.entity.embedded

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
class BoardHitId : Serializable {
  var boardId: Long? = null
  var userId: Long? = null
}