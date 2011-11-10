package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import KDB._
import sk.gresak.domain.Owner

import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.session.Database.threadLocalSession
import javax.sql.rowset.serial.SerialClob

object OwnerDAO extends BaseDAO {

  def updateOwnersGeocodes(geocodes: List[(Long, String)]) = {
    withTransaction {
      geocodes map {
        x => updateGeocodesByOwnerId(x._1, x._2)
      }
    }
  }

  def updateGeocodesByOwnerId(z: Long, gc: String) {
    val query = vlastnik.where(_.id_vlastnik === z.bind).map(_.clob_geocode_json)
    query.update(new SerialClob(gc.toCharArray))
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
        owner <- vlastnik if owner.id_report === id_report
      } yield owner.id_vlastnik ~ owner.tx_adresa
      qx.list
    }

}