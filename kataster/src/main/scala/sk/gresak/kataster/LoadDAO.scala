package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import javax.sql.rowset.serial.SerialClob

class LoadDAO {

  private def insertFileName(fName: String): Option[Long] = withSession {
    if (1 == nacitanie.tx_subor.insert(fName)) insertedId else None
  }

  def loadReportLines(fName: String, lines: Array[String]): Option[Int] = withSession {
    val id: Option[Long] = insertFileName(fName)
    if (id == None) None
    else {
      import riadok._
      val g = (1 to lines.size).map(i => (id.get, i, lines(i - 1)))
      (id_nacitanie ~ int_cislo_riadok ~ tx_riadok).insertAll(g: _*)
    }
  }
  def insertRawReport(fName: String, report:String): Option[Long] = withSession {
    if (1 == (rawReport.tx_subor~rawReport.clob_report).insert(fName, new SerialClob(report.toCharArray))) insertedId else None
  }


}