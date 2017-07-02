package net.ingtra.hanalmot.component


private[hanalmot] class Result(string: String, tokenArray: Array[HanalmotToken]) {
  val text: String = string
  var tokens: Array[HanalmotToken] = tokenArray

  def printResult(): Unit = {
    tokens.foreach(println)
  }
}
