package net.ingtra.hanalmot.util


private[hanalmot] object MapUtil {
  def normalizeWithLog[T](map: Map[T, Double]) = {
    map.mapValues((score: Double) => Math.log1p(score))
  }

  def normalizeWithSum[T](map: Map[T, Double]) = {
    val sum = map.values.sum
    if (sum != 0)
      map.mapValues((score: Double) => score / sum)
    else
      map
  }

  def dropToLow[T](map: Map[T, Double]) = {
    val sorted = map.values.toList.sorted
    val limit = sorted(sorted.size / 2) / 2
    map
  }
}
