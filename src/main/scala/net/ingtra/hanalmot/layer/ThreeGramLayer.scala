package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, Prediction}
import net.ingtra.hanalmot.util.DictionaryUtil

private[hanalmot] object ThreeGramLayer {
  def apply(prediction: Prediction): Prediction = {
    val charArray = Array("<시작>", "<시작>") ++ prediction.chars ++ Array("<끝>", "<끝>")
    for (element <- charArray.dropRight(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getLeft3gram(gram)
        val candidates = new Candidates(count).normalizeWithLog().normalizeWithSum()
        prediction.candidatesArray(index) += candidates * 1.5
      }
    }
    for (element <- charArray.drop(1).dropRight(1).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getCenter3gram(gram)
        val candidates = new Candidates(count).normalizeWithLog().normalizeWithSum()
        prediction.candidatesArray(index) += candidates
      }
    }
    for (element <- charArray.drop(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getRight3gram(gram)
        val candidates = new Candidates(count).normalizeWithLog().normalizeWithSum()
        prediction.candidatesArray(index) += candidates * 1.5
      }
    }

    prediction.candidatesArray = prediction.candidatesArray.map(_.normalizeWithSum())
    prediction
  }
}
