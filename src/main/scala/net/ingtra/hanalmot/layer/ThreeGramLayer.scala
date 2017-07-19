package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, HanalmotToken, Prediction}
import net.ingtra.hanalmot.util.DictionaryUtil

private[hanalmot] object ThreeGramLayer {
  def apply(prediction: Prediction): Prediction = {
    val charArray = Array("<시작>", "<시작>") ++ prediction.chars ++ Array("<끝>", "<끝>")
    for (element <- charArray.dropRight(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val gramMap = DictionaryUtil.getLeft3gram(gram)
        prediction.candidatesArray(index) += new Candidates(gramMap) * 1.5
      }
    }
    for (element <- charArray.drop(1).dropRight(1).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val gramMap = DictionaryUtil.getCenter3gram(gram)
        prediction.candidatesArray(index) += new Candidates(gramMap)
      }
    }
    for (element <- charArray.drop(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val gramMap = DictionaryUtil.getRight3gram(gram)
        prediction.candidatesArray(index) += new Candidates(gramMap) * 1.5
      }
    }
    prediction.candidatesArray.foreach(_.normalizeWithSum())
    prediction.candidatesArray.foreach(_.remainTop(3))
    prediction
  }
}
