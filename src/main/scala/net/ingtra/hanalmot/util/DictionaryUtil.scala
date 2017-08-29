package net.ingtra.hanalmot.util

import java.sql.{PreparedStatement, ResultSet, Statement}

import net.ingtra.hanalmot.component.HanalmotToken

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


private[hanalmot] object DictionaryUtil {
  val tokensArray: Array[HanalmotToken] = getTokens()

  def getLeft3gram(gram: String): Map[Seq[HanalmotToken], Double] = get3gram(gram, "left")
  def getCenter3gram(gram: String): Map[Seq[HanalmotToken], Double] = get3gram(gram, "center")
  def getRight3gram(gram: String): Map[Seq[HanalmotToken], Double] = get3gram(gram, "right")

  def getLeftPos(): Map[String, Map[String, Double]] = getPos("left")
  def getRightPos(): Map[String, Map[String, Double]] = getPos("right")

  def getTokens(): Array[HanalmotToken] = {
    val tempList = ListBuffer[HanalmotToken]()
    val resultSet: ResultSet = DBConnector.executeQuery("SELECT id, letter, pos FROM tokens;")
    while (resultSet.next()) {
      val id: Int = resultSet.getInt("id")
      val letter: String = resultSet.getString("letter")
      val pos: String = resultSet.getString("pos")

      val tokenObject: HanalmotToken = HanalmotToken(letter, pos)

      tempList.append(tokenObject)
    }
    resultSet.getStatement().close()

    tempList.toArray[HanalmotToken]
  }

  private def get3gram(gram: String, direction: String) = {
    val tempMap = mutable.Map[Seq[HanalmotToken], Double]()

    val resultSet: ResultSet = DBConnector.executeQuery(s"SELECT tokens, count FROM $direction" + s"_3gram WHERE letter = ?;", Seq(gram))
    while (resultSet.next()) {
      val tokens = resultSet.getString("tokens").split(',').map((token) => tokensArray(token.toInt)).toSeq
      val count = resultSet.getInt("count").toDouble
      tempMap.put(tokens, count)
    }
    resultSet.getStatement().close()
    val resultMap = tempMap.toMap[Seq[HanalmotToken], Double]
    MapUtil.normalizeWithSum(MapUtil.normalizeWithLog(resultMap))
  }

  private def getPos(direction: String): Map[String, Map[String, Double]] = {
    val tempMap = mutable.ListBuffer[(String, String, Double)]()
    val resultSet = DBConnector.executeQuery(s"SELECT pos, target, count from $direction" + "_pos;")
    while (resultSet.next()) {
      val pos = resultSet.getString("pos")
      val target = resultSet.getString("target")
      val count = resultSet.getInt("count").toDouble
      tempMap.append((pos, target, count))
    }
    resultSet.getStatement().close()
    tempMap.groupBy(_._1).mapValues(_.map((e) => e._2 -> e._3).toMap)
  }
}
