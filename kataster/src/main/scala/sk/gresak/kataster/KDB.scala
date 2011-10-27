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


  object vlastnik extends Table[(Long, Long, String, Int, String, String, Date, String, String, String)]("vlastnik") {

    def id_vlastnik = column[Long]("id_vlastnik", O.PrimaryKey, O.AutoInc)

    def id_report = column[Long]("id_report")

    def tx_pravny_vztah = column[String]("tx_pravny_vztah")

    def int_por_cislo = column[Int]("int_por_cislo")

    def tx_meno_adresa = column[String]("tx_meno_adresa")

    def tx_podiel = column[String]("tx_podiel")

    def dt_narodenie = column[Date]("dt_narodenie")

    def tx_ico = column[String]("tx_ico")

    def tx_plomba = column[String]("tx_plomba")

    def tx_nadobudnutie = column[String]("tx_nadobudnutie")

    def reportKey = foreignKey("id_report_fk", id_report, report)(_.id_report)

    def * = id_vlastnik ~ id_report ~ tx_pravny_vztah ~ int_por_cislo ~ tx_meno_adresa ~ tx_podiel ~ dt_narodenie ~ tx_ico ~ tx_plomba ~ tx_nadobudnutie

  }


  def insertedId: Some[Long] = {
    val insertedIdFunction = SimpleFunction.nullary[Long]("last_insert_id")
    new Some(Query(insertedIdFunction).first)
  }

}