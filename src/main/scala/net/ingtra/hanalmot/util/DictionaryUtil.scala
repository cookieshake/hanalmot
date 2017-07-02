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

  def getLeftPos(): Map[(String, String), Double] = getPos("left")
  def getRightPos(): Map[(String, String), Double] = getPos("right")

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
    tempMap.toMap[Seq[HanalmotToken], Double]
  }

  private def getPos(direction: String): Map[(String, String), Double] = {
    val tempMap = mutable.Map[(String, String), Double]()
    val resultSet = DBConnector.executeQuery(s"SELECT pos, target, count from $direction" + "_pos;")
    while (resultSet.next()) {
      val pos = resultSet.getString("pos")
      val target = resultSet.getString("target")
      val count = resultSet.getInt("count").toDouble
      tempMap.put((pos, target), count)
    }
    tempMap.toMap
  }
}
