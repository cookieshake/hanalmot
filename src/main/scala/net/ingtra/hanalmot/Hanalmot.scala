package net.ingtra.hanalmot

import net.ingtra.hanalmot.component.{HanalmotToken, Prediction}
import net.ingtra.hanalmot.layer._

object Hanalmot {
  def tokenize(text: String): Array[HanalmotToken] = {
    var prediction = new Prediction(text.replace("\n", " "))
    prediction = SureLayer(prediction)
    prediction = ThreeGramLayer(prediction)
    prediction = FillNNPLayer(prediction)
    prediction = PosLayer(prediction)
    var result = PredictionToResultLayer(prediction)
    result = NNPCorrectionLayer(result)
    result = ConcatenationLayer(result)
    result.tokens
  }

  def nouns(text: String): Array[String] =
    tokenize(text).filter((token) => token.pos.startsWith("N")).map(_.letter)

  def main(args: Array[String]): Unit = args.length match {
    case 0 => println("Please Say More")
    case default => tokenize(args.mkString(" ")).foreach(println)
  }
}
