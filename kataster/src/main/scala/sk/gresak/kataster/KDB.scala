package sk.gresak.kataster

import org.scalaquery.ql.extended.{ExtendedTable => Table}
import java.sql.{Timestamp, Clob, Date}
import org.scalaquery.session.Database

object KDB {

  private var db: Database = null

  def setForUrl(dbUrl: String, user: String = null, password: String = null, driver: String = null) {
    db = Database.forURL(url = dbUrl, user = user, password = password, driver = driver)
  }

  def withSession[T](f: => T): T = db.withSession(f)

  def withTransaction[T](f: => T): T = db.withTransaction(f)

  object report extends Table[(Long, String, Date, Timestamp, Clob)]("report") {

    def id_report = column[Long]("id_report", O.PrimaryKey, O.AutoInc)

    def tx_subor = column[String]("tx_subor")

    def dt_aktualizacia = column[Date]("dt_aktualizacia")

    def dt_vyhotovenie = column[Timestamp]("dt_vyhotovenie")

    def clob_report = column[Clob]("clob_report")

    def * = id_report ~ tx_subor ~ dt_aktualizacia ~ dt_vyhotovenie ~ clob_report

  }

  object vlastnik extends Table[(Long, Long, String, Int, String, String, String, String, String, String, String, Int, Clob, String,
    String, Int, Int, Date, String, String, String, String)]("vlastnik") {

    def id_vlastnik = column[Long]("id_vlastnik", O.PrimaryKey, O.AutoInc)

    def id_report = column[Long]("id_report")

    def tx_pravny_vztah = column[String]("tx_pravny_vztah")

    def int_por_cislo = column[Int]("int_por_cislo")

    def tx_meno = column[String]("tx_meno")

    def tx_priezvisko = column[String]("tx_priezvisko")

    def tx_rodeny = column[String]("tx_rodeny")

    def tx_adresa = column[String]("tx_adresa")

    def tx_formatted_address = column[String]("tx_formatted_address")

    def tx_locality = column[String]("tx_locality")

    def tx_postal_code = column[String]("tx_postal_code")

    def int_address_count = column[Int]("int_address_count")

    def clob_geocode_json = column[Clob]("clob_geocode_json")

    def tx_psc = column[String]("tx_psc")

    def tx_stat = column[String]("tx_stat")

    def int_podiel1 = column[Int]("int_podiel1")

    def int_podiel2 = column[Int]("int_podiel2")

    def dt_narodenie = column[Date]("dt_narodenie")

    def tx_ico = column[String]("tx_ico")

    def tx_plomba = column[String]("tx_plomba")

    def tx_nadobudnutie = column[String]("tx_nadobudnutie")

    def tx_poznamky = column[String]("tx_poznamky")

    def reportKey = foreignKey("id_report_fk", id_report, report)(_.id_report)

    def * = id_vlastnik ~ id_report ~ tx_pravny_vztah ~ int_por_cislo ~
      tx_meno ~ tx_priezvisko ~ tx_rodeny ~ tx_adresa ~ tx_formatted_address ~ tx_locality ~ tx_postal_code ~
      int_address_count ~ clob_geocode_json ~ tx_psc ~ tx_stat ~
      int_podiel1 ~ int_podiel2 ~ dt_narodenie ~ tx_ico ~ tx_plomba ~ tx_nadobudnutie ~ tx_poznamky

  }

}