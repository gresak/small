package sk.gresak.kataster

import java.util.Properties
import java.io.FileInputStream

object RunGeocodes {
  def main(args: Array[String]) {
    KDB.setDb(args(0))
    val reportProp = new Properties
    reportProp.load(new FileInputStream(args(1)))
    val reportId: Long = Integer.parseInt(reportProp.getProperty("id_report")).toLong
    Geocodes.downloadGeocodesByIdReport(reportId)
    Geocodes.updateAddressFromGeocodes(reportId)
  }

}


