package net.ingtra.hanalmot.component

import net.ingtra.hanalmot.util.MapUtil

import scala.collection.mutable


private[hanalmot] class Candidates(map: Map[Seq[HanalmotToken], Double]) {
  var content: Map[Seq[HanalmotToken], Double] = map
  var isMutable: Boolean = true

  def setMutable(boolean: Boolean): Candidates = {
    isMutable = boolean
    this
  }

  def remainTop(number: Int): Candidates = {
    content = content.toSeq.sortBy(_._2).takeRight(number).toMap
    this
  }

  def normalizeWithLog(): Candidates = {
    content = MapUtil.normalizeWithLog(content)
    this
  }

  def normalizeWithSum(): Candidates = {
    content = MapUtil.normalizeWithSum(content)
    this
  }

  def +(candidates: Candidates): Candidates = {
    val newContent = mutable.Map[Seq[HanalmotToken], Double]()
    content.map((row) => newContent.put(row._1, row._2))
    candidates.content.map((row) => newContent.put(row._1, newContent.getOrElse[Double](row._1, 0) + row._2))

    new Candidates(newContent.toMap)
  }

  def *(double: Double): Candidates = {
    content = content.mapValues(_ * double)
    this
  }

  def /(double: Double): Candidates = {
    content = content.mapValues(_ / double)
    this
  }

  override def toString: String = content.toSeq.sortBy(_._2).reverse.toString()
}
