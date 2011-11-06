package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import KDB._
import sk.gresak.domain.Owner

import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.session.Database.threadLocalSession
import javax.sql.rowset.serial.SerialClob

object OwnerDAO extends BaseDAO {

  def updateOwnersGeocodes(geocodes: List[(Long, String)]) = {
    /*
        val q = vlastnik.where(_.id_vlastnik === 5.bind).map(_.clob_geocode_json)
        q.update(new SerialClob("hhh".toCharArray))
    */
  }

  def updateOwnersGeocodes1() = withSession {
    //vlastnik.id_vlastnik.===()
/*
    val q=for (owner<-vlastnik if owner.id_vlastnik===)

    val q8 = for(u <- Users if u.last.isNull) yield u.first ~ u.last
    println("q8: " + q8.updateStatement)

*/

  }

  def updateOwnersGeocodes2() = withSession {
    val z: Long = 5
    val q7 = vlastnik.where(_.id_vlastnik === z.bind).map(_.clob_geocode_json)
    val updated2 = q7.update(new SerialClob("hhh".toCharArray))
    println("Updated " + updated2 + " row(s)")

  }


  def insertOwners(owners: Array[Owner]) = withSession {
    val tuples = owners map (o => Owner.unapply(o).get)
    import vlastnik._
    (id_report ~ tx_pravny_vztah ~ int_por_cislo ~ tx_meno ~ tx_priezvisko ~ tx_rodeny ~ tx_adresa ~ tx_psc ~ tx_stat ~ int_podiel1 ~ int_podiel2 ~
      dt_narodenie ~ tx_ico ~ tx_plomba ~ tx_nadobudnutie ~ tx_poznamky).insertAll(tuples: _*)
  }

  def readOwners(id_report: Long): List[(Long, String)] =
    withSession {
      val qx = for {
        owner <- vlastnik if owner.id_report == id_report
      } yield owner.id_vlastnik ~ owner.tx_adresa
      qx.list
    }

}