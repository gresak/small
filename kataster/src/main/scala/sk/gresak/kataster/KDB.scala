package sk.gresak.kataster

import org.scalaquery.ql.{Query, SimpleFunction}
import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import java.sql.{Clob, Date}

object KDB {

  import org.scalaquery.session.Database

  private var db: Database = null

  def setForUrl(dbUrl: String, user: String = null, password: String = null, driver: String = null) {
    db = Database.forURL(url = dbUrl, user = user, password = password, driver = driver)
  }

  def withSession[T](f: => T): T = db.withSession(f)

  object nacitanie extends Table[(Long, Date, String)]("nacitanie") {

    def id_nacitanie = column[Long]("id_nacitanie", O.PrimaryKey, O.AutoInc)

    def dt_vznik = column[Date]("dt_vznik")

    def tx_subor = column[String]("tx_subor")

    def * = id_nacitanie ~ dt_vznik ~ tx_subor

  }

  object riadok extends Table[(Long, Long, Int, String)]("riadok") {

    def id_riadok = column[Long]("id_riadok", O.PrimaryKey, O.AutoInc)

    def id_nacitanie = column[Long]("id_nacitanie")

    def int_cislo_riadok = column[Int]("int_cislo_riadok")

    def tx_riadok = column[String]("tx_riadok")

    def nacitanieKey = foreignKey("id_nacitanie_fk", id_nacitanie, nacitanie)(_.id_nacitanie)

    def * = id_riadok ~ id_nacitanie ~ int_cislo_riadok ~ tx_riadok

  }

  object rawReport extends Table[(Long, Date, String, Clob)]("raw_report") {

    def id_raw_report = column[Long]("id_raw_report", O.PrimaryKey, O.AutoInc)

    def dt_vznik = column[Date]("dt_vznik")

    def tx_subor = column[String]("tx_subor")

    def clob_report = column[Clob]("clob_report")

    def * = id_raw_report ~ dt_vznik ~ tx_subor ~ clob_report

  }

  def insertedId: Some[Long] = {
    val insertedIdFunction = SimpleFunction.nullary[Long]("last_insert_id")
    new Some(Query(insertedIdFunction).first)
  }

}