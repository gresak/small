package sk.gresak.kataster

import java.io.FileInputStream
import java.util.Properties

object RunKataster {
  def main(args: Array[String]) {
    val ps = new Properties
    ps.load(new FileInputStream(args(0)))
    KDB.setForUrl(ps.getProperty("dbUrl"), ps.getProperty("dbUser"), ps.getProperty("dbPassword"), ps.getProperty("dbDriver"))
    val r = new OwnershipReport(ps.getProperty("zostava"))
    r.process()
  }

}


