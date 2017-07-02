package net.ingtra.hanalmot.util

import java.sql.{Connection, DriverManager, ResultSet, Statement}

private[hanalmot] object DBConnector {
  private val DBFilePath: String = "dictionary.db"
  private val connection: Connection = DriverManager.getConnection("jdbc:sqlite::resource:" + DBFilePath)

  def executeQuery(sql: String, parameters: Seq[String] = Seq()): ResultSet = {
    val preparedStatement = connection.prepareStatement(sql)
    for ((parameter, index) <- parameters.zipWithIndex) {
      preparedStatement.setString(index + 1, parameter)
    }
    preparedStatement.executeQuery()
  }
}


