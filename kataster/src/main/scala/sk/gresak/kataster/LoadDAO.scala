package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import javax.sql.rowset.serial.SerialClob
import java.sql.{Timestamp, Date}

object LoadDAO extends BaseDAO {

  def insertRawReport(fName: String, actualized: Date, created: Timestamp, reportStr: String): Long = withSession {
    import report._
    if (1 == (tx_subor ~ dt_aktualizacia ~ dt_vyhotovenie ~ clob_report).insert(
      fName, actualized, created, new SerialClob(reportStr.toCharArray))
    ) insertedId.get
    else throw new Exception("Ulozenie reportu do db sa nepodarilo, report = " +
      fName + " actualized = " + actualized + " created = " + created
    )
  }


}