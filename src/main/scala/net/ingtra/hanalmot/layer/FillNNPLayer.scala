package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, HanalmotToken, Prediction}

private[hanalmot] object FillNNPLayer {
  private def nnpCandidate(letter: String) = {
    new Candidates(Map(Seq(HanalmotToken(letter, "NNP")) -> 1.0)).setMutable(false)
  }

  def apply(prediction: Prediction): Prediction = {
    for (element <- prediction.chars.zipWithIndex) {
      val char = element._1
      val index = element._2

      if (prediction.candidatesArray(index).content.isEmpty) {
        prediction.candidatesArray(index) = nnpCandidate(char)
      }
    }
    prediction
  }
}
