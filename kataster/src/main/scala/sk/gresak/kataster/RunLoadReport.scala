package sk.gresak.kataster

import java.io.FileInputStream
import java.util.Properties

object RunLoadReport {

  def main(args: Array[String]) {
    KDB.setDb(args(0))
    val reportProp = new Properties
    reportProp.load(new FileInputStream(args(1)))
    val r = new OwnershipReport(reportProp.getProperty("zostava"), args(2))
    r.process()
  }

}


