package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{HanalmotToken, Result}

import scala.collection.mutable


object ConcatenationLayer {
  def apply(result: Result): Result = {
    val finalToken = mutable.ListBuffer[HanalmotToken]()
    val tokenBuffer = mutable.ListBuffer[HanalmotToken]()

    for (token <- result.tokens) {
      if (tokenBuffer.nonEmpty && token.pos != tokenBuffer.last.pos) {
        val newLetter = tokenBuffer.map(_.letter).reduce(_ + _)
        val newPos = tokenBuffer.last.pos
        finalToken.append(HanalmotToken(newLetter, newPos))
        tokenBuffer.clear()
      }
      tokenBuffer.append(token)
    }

    val newLetter = tokenBuffer.map(_.letter).reduce(_ + _)
    val newPos = tokenBuffer.last.pos
    finalToken.append(HanalmotToken(newLetter, newPos))
    tokenBuffer.clear()

    result.tokens = finalToken.toArray
    result
  }
}
