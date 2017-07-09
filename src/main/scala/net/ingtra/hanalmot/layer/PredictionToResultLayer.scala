package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Prediction, Result}

private[hanalmot] object PredictionToResultLayer {
  def apply(prediction: Prediction): Result = {
    prediction.candidatesArray.foreach(_.remainTop(1))
    new Result(prediction.chars.reduceLeft(_ + _), prediction.candidatesArray.flatMap(_.content.keys.head).filter(_.pos != "ZNO"))
  }
}
