package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, HanalmotToken, Prediction}

private[hanalmot] object SureLayer {
  private val alphabet: Set[String] = (('A' to 'Z') ++ ('a' to 'z')).toSet.map((c: Char) => c.toString)
  private val number: Set[String] = ('0' to '9').toSet.map((c: Char) => c.toString)

  private def sureCandidate(letter: String, pos: String) = {
    new Candidates(Map(Seq(HanalmotToken(letter, pos)) -> 1.0)).setMutable(false)
  }

  private def convert(char: String): Candidates = {
    if (char == " ")
      sureCandidate(char, "ZSP")
    else if (char == "." || char == "?" || char == "!")
      sureCandidate(char, "SF")
    else if (char == "," || char == ":" || char == "/")
      sureCandidate(char, "SP")
    else if (char == '"'.toString || char == '“'.toString || char == "'")
      sureCandidate(char, "SS")
    else if (alphabet.contains(char))
      sureCandidate(char, "SL")
    else if (number.contains(char))
      sureCandidate(char, "SN")
    else if ('ㄱ' <= char.toCharArray()(0) && char.toCharArray()(0) <= 'ㅎ')
      sureCandidate(char, "ZKP")
    else if ('ㅏ' <= char.toCharArray()(0) && char.toCharArray()(0) <= 'ㅣ')
      sureCandidate(char, "ZKP")
    else
      new Candidates(Map())
  }

  def apply(prediction: Prediction): Prediction = {
    for (element <- prediction.chars.zipWithIndex) {
      val char = element._1
      val index = element._2

      prediction.candidatesArray(index) = convert(char)
    }
    prediction
  }
}
