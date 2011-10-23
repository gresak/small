package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import javax.sql.rowset.serial.SerialClob
import java.sql.{Timestamp, Date}

class LoadDAO {

  def insertRawReport(fName: String, actualized: Date, created: Timestamp, reportStr: String): Option[Long] = withSession {
    import report._
    if (1 == (tx_subor ~ dt_aktualizacia ~ dt_vyhotovenie ~ clob_report).insert(
      fName, actualized, created, new SerialClob(reportStr.toCharArray))
    ) insertedId
    else None
  }


}