package net.ingtra.hanalmot.component

private[hanalmot] class Prediction(text: String) {
  val chars: Array[String] = text.split("")
  var candidatesArray: Array[Candidates] = Array.fill(chars.length)(new Candidates(Map()))

  def printPrediction(): Unit = {
    candidatesArray.foreach(println)
  }
}
