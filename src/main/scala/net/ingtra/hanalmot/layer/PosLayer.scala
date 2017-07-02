package net.ingtra.hanalmot.layer

import net.ingtra.hanalmot.component.{HanalmotToken, Prediction}
import net.ingtra.hanalmot.util.{DictionaryUtil, MapUtil}

import scala.collection.mutable

object PosLayer {
  def apply(prediction: Prediction): Prediction = {
    var posLeft = DictionaryUtil.getLeftPos()
    posLeft = MapUtil.normalizeWithLog(posLeft)
    posLeft = MapUtil.normalizeWithSum(posLeft)
    var posRight = DictionaryUtil.getRightPos()
    posRight = MapUtil.normalizeWithLog(posRight)
    posRight = MapUtil.normalizeWithSum(posRight)
    val candidatesArray = prediction.candidatesArray

    for (i <- candidatesArray.indices) {
      var leftPosMap: Map[String, Double] = null
      if (i == 0) {
        leftPosMap = Map("ZST" -> 1.0)
      } else {
        leftPosMap = candidatesArray(i - 1).content.map((element) => (element._1.last.pos, element._2))
        leftPosMap = MapUtil.normalizeWithLog(leftPosMap)
        leftPosMap = MapUtil.normalizeWithSum(leftPosMap)
      }

      var rightPosMap: Map[String, Double] = null
      if (i == candidatesArray.length - 1) {
        rightPosMap = Map("ZED" -> 1.0)
      } else {
        rightPosMap = candidatesArray(i + 1).content.map((element) => (element._1.head.pos, element._2))
        rightPosMap = MapUtil.normalizeWithLog(rightPosMap)
        rightPosMap = MapUtil.normalizeWithSum(rightPosMap)
      }

      val newMap = mutable.Map[Seq[HanalmotToken], Double]()
      for (candidate <- candidatesArray(i).content) {
        val myLeftPos = candidate._1.head.pos
        val myRightPos = candidate._1.last.pos

        val leftSum = posLeft.filter((element) =>
          element._1._1 == myLeftPos && leftPosMap.contains(element._1._2)).values.sum
        val rightSum = posRight.filter((element) =>
          element._1._1 == myRightPos && rightPosMap.contains(element._1._2)).values.sum

        newMap.put(candidate._1, candidate._2 * (leftSum + rightSum))
      }
      candidatesArray(i).content = newMap.toMap

    }
    prediction.candidatesArray = prediction.candidatesArray.map(_.normalizeWithSum())
    prediction
  }
}
