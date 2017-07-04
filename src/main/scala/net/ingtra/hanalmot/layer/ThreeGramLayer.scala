package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, HanalmotToken, Prediction}
import net.ingtra.hanalmot.util.DictionaryUtil

private[hanalmot] object ThreeGramLayer {
  private def nnpCandidate(letter: String) = {
    new Candidates(Map(Seq(HanalmotToken(letter, "NNP")) -> 1.4))
  }

  def apply(prediction: Prediction): Prediction = {
    val charArray = Array("<시작>", "<시작>") ++ prediction.chars ++ Array("<끝>", "<끝>")
    for (element <- charArray.dropRight(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getLeft3gram(gram)
        if (count.isEmpty) {
          prediction.candidatesArray(index) += nnpCandidate(gram(2).toString)
        } else {
          prediction.candidatesArray(index) += new Candidates(count).normalizeWithLog().normalizeWithSum() * 1.5
        }
      }
    }
    for (element <- charArray.drop(1).dropRight(1).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getCenter3gram(gram)
        if (count.isEmpty) {
          prediction.candidatesArray(index) += nnpCandidate(gram(1).toString)
        } else {
          prediction.candidatesArray(index) += new Candidates(count).normalizeWithLog().normalizeWithSum()
        }
      }
    }
    for (element <- charArray.drop(2).sliding(3).zipWithIndex) {
      val gram = element._1.reduceLeft(_ + _)
      val index = element._2
      if (prediction.candidatesArray(index).isMutable) {
        val count = DictionaryUtil.getRight3gram(gram)
        if (count.isEmpty) {
          prediction.candidatesArray(index) += nnpCandidate(gram(0).toString)
        } else {
          prediction.candidatesArray(index) += new Candidates(count).normalizeWithLog().normalizeWithSum() * 1.5
        }
      }
    }

    prediction.candidatesArray = prediction.candidatesArray.map(_.normalizeWithSum())
    prediction
  }
}
