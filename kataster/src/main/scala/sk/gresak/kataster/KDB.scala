package sk.gresak.kataster

import org.scalaquery.ql.{Query, SimpleFunction}
import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import java.sql.{Timestamp, Clob, Date}

object KDB {

  import org.scalaquery.session.Database

  private var db: Database = null

  def setForUrl(dbUrl: String, user: String = null, password: String = null, driver: String = null) {
    db = Database.forURL(url = dbUrl, user = user, password = password, driver = driver)
  }

  def withSession[T](f: => T): T = db.withSession(f)

  object report extends Table[(Long, String, Date, Timestamp, Clob)]("report") {

    def id_report = column[Long]("id_report", O.PrimaryKey, O.AutoInc)

    def tx_subor = column[String]("tx_subor")

    def dt_aktualizacia = column[Date]("dt_aktualizacia")

    def dt_vyhotovenie = column[Timestamp]("dt_vyhotovenie")

    def clob_report = column[Clob]("clob_report")

    def * = id_report ~ tx_subor ~ dt_aktualizacia ~ dt_vyhotovenie ~ clob_report

  }

  def insertedId: Some[Long] = {
    val insertedIdFunction = SimpleFunction.nullary[Long]("last_insert_id")
    new Some(Query(insertedIdFunction).first)
  }

}