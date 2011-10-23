package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import javax.sql.rowset.serial.SerialClob
import java.sql.Date

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
  def insertRawReport(fName: String, created: Date, report:String): Option[Long] = withSession {
    if (1 == (rawReport.tx_subor~rawReport.dt_vznik~rawReport.clob_report).insert(fName, created, new SerialClob(report.toCharArray))) insertedId else None
  }


}