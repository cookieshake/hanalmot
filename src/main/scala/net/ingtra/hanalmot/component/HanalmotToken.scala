package net.ingtra.hanalmot.component

case class HanalmotToken(var letter: String, var pos: String) {
  override def toString: String = s"($letter,$pos)"
}
