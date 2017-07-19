package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{HanalmotToken, Result}
import scala.collection.mutable

private[hanalmot] object NNPCorrectionLayer {
  def apply(result: Result): Result = {
    var nounChunk = mutable.ListBuffer[HanalmotToken]()
    for (i <- result.tokens.indices) {
      val token = result.tokens(i)
      if (token.pos.startsWith("N")) {
        nounChunk.append(token)
      } else {
        if (nounChunk.count((token) => token.pos == "NNP") > 0) {
          nounChunk.foreach(_.pos = "NNP")
        }
        nounChunk.clear()
      }
    }
    nounChunk.clear()
    result
  }

}
