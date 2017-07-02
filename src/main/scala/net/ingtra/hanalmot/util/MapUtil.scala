package net.ingtra.hanalmot.util


private[hanalmot] object MapUtil {
  def normalizeWithLog[T](map: Map[T, Double]) = {
    map.mapValues((score: Double) => Math.log(score + 1))
  }
  def normalizeWithSum[T](map: Map[T, Double]) = {
    val sum = map.values.sum
    map.mapValues((score: Double) => score / sum)
  }
}
