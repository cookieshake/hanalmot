package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{Candidates, HanalmotToken, Prediction}
import net.ingtra.hanalmot.util.{DictionaryUtil, MapUtil}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object PosLayer {
  val posLeftDic = DictionaryUtil.getLeftPos()
    .mapValues(MapUtil.normalizeWithLog).mapValues(MapUtil.normalizeWithSum).mapValues(_.withDefaultValue[Double](0))
    .withDefaultValue(Map().withDefaultValue[Double](0))
  val posRightDic = DictionaryUtil.getRightPos()
    .mapValues(MapUtil.normalizeWithLog).mapValues(MapUtil.normalizeWithSum).mapValues(_.withDefaultValue[Double](0))
    .withDefaultValue(Map().withDefaultValue[Double](0))

  def apply(prediction: Prediction): Prediction = {
    val candidatesArray = prediction.candidatesArray
    val newCandidatesArray: ListBuffer[Candidates] = mutable.ListBuffer()

    for (i <- candidatesArray.indices) {
      var formerPosMap: Map[String, Double] = null
      if (i == 0) {
        formerPosMap = Map("ZST" -> 1.0)
      } else {
        formerPosMap = candidatesArray(i - 1).content.map((element) => (element._1.last.pos, element._2))
      }

      var followingPosMap: Map[String, Double] = null
      if (i == candidatesArray.length - 1) {
        followingPosMap = Map("ZED" -> 1.0)
      } else {
        followingPosMap = candidatesArray(i + 1).content.map((element) => (element._1.head.pos, element._2))
      }

      val newMap = mutable.Map[Seq[HanalmotToken], Double]()
      for (candidate <- candidatesArray(i).content) {
        val myLeftPos = candidate._1.head.pos
        val myRightPos = candidate._1.last.pos

        val leftSum = formerPosMap.map((e) => posRightDic(e._1)(myLeftPos) * e._2).sum
        val rightSum = followingPosMap.map((e) => posLeftDic(e._1)(myRightPos) * e._2).sum
        newMap.put(candidate._1, candidate._2 * (leftSum + rightSum))
      }
      newCandidatesArray.append(new Candidates(newMap.toMap))
    }
    prediction.candidatesArray = newCandidatesArray.toArray
    prediction.candidatesArray = prediction.candidatesArray.map(_.normalizeWithSum())
    prediction
  }
}
