package sk.gresak.kataster

import java.io.FileInputStream
import java.util.Properties

object RunGeocodes {
  def main(args: Array[String]) {
    val dbProp = new Properties
    dbProp.load(new FileInputStream(args(0)))
    KDB.setForUrl(dbProp.getProperty("dbUrl"), dbProp.getProperty("dbUser"), dbProp.getProperty("dbPassword"), dbProp.getProperty("dbDriver"))
    //Geocodes.reloadByIdReport(1.toLong)
    Geocodes.updateFormattedAddress(1.toLong)
  }

}


