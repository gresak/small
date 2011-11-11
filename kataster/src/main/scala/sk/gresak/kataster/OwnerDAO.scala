package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import KDB._
import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.session.Database.threadLocalSession
import javax.sql.rowset.serial.SerialClob
import java.sql.Clob
import net.liftweb.json._
import sk.gresak.domain.{ResponseG, Owner}

object OwnerDAO extends BaseDAO {

  implicit val formats = DefaultFormats

  def updateOwnersGeocodes(geocodes: List[(Long, String)]) = {
    withTransaction {
      geocodes map {
        x => updateGeocodesByOwnerId(x._1, x._2)
      }
    }
  }

  def updateOwnersFormattedAddresses(addresses: List[(Long, String, String, String, Int)]) = {
    withTransaction {
      addresses map {
        x => updateFormattedAddressesByOwnerId(x._1, x._2, x._3, x._4, x._5)
      }
    }
  }

  def updateGeocodesByOwnerId(z: Long, gc: String) {
    val query = vlastnik.where(_.id_vlastnik === z.bind).map(_.clob_geocode_json)
    query.update(new SerialClob(gc.toCharArray))
  }

  def updateFormattedAddressesByOwnerId(ownerId: Long, formattedAddress: String, locality: String, postalCode: String, addressCount: Int) {
    val query = vlastnik.where(_.id_vlastnik === ownerId.bind).map {
      v => v.tx_formatted_address ~ v.tx_locality ~ v.tx_postal_code ~ v.int_address_count
    }
    query.update(formattedAddress, locality, postalCode, addressCount)
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
        owner <- vlastnik if owner.id_report === id_report && owner.tx_adresa.length > 5 && !(owner.tx_adresa.toUpperCase.like("%NESLUŠA%"))
      } yield owner.id_vlastnik ~ owner.tx_adresa
      println(qx.selectStatement)
      qx.list
    }

  def readGeocodes(id_report: Long): List[(Long, ResponseG)] = {
    val list0: List[(Long, Clob)] = withSession {
      val qx = for {
        owner <- vlastnik if owner.id_report === id_report && owner.clob_geocode_json.isNotNull
      } yield owner.id_vlastnik ~ owner.clob_geocode_json
      println(qx.selectStatement)
      qx.list
    }
    list0.map(x => (x._1, JsonParser.parse(x._2.getCharacterStream).extract[ResponseG]))
  }

}