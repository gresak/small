package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import java.sql.Date
import sk.gresak.domain.Owner

class OwnerDAO {

  def insertOwners(owners: Array[Owner]): Unit = withSession {
    val tuples: Array[(Long, String, Int, String, String, String, String, String, String, Int, Int, Date, String, String, String, String)] = owners map (o => {
      Owner.unapply(o).get
    })
    import vlastnik._
    (id_report ~ tx_pravny_vztah ~ int_por_cislo ~ tx_meno ~ tx_priezvisko ~ tx_rodeny ~ tx_adresa ~ tx_psc ~ tx_stat ~ int_podiel1 ~ int_podiel2 ~
      dt_narodenie ~ tx_ico ~ tx_plomba ~ tx_nadobudnutie ~ tx_poznamky).insertAll(tuples: _*)
  }


}