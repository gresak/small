package sk.gresak.kataster

import java.io.FileInputStream
import java.util.Properties

object RunLoadReport {
  def main(args: Array[String]) {
    val dbProp = new Properties
    dbProp.load(new FileInputStream(args(0)))
    KDB.setForUrl(dbProp.getProperty("dbUrl"), dbProp.getProperty("dbUser"), dbProp.getProperty("dbPassword"), dbProp.getProperty("dbDriver"))
    val otherProp = new Properties
    otherProp.load(new FileInputStream(args(1)))
    val r = new OwnershipReport(otherProp.getProperty("zostava"), args(2))
    r.process()
  }

}


