package sk.gresak.kataster

import org.scalatest.FunSuite
import java.util.Properties
import java.io.FileInputStream

class OwnerUpdateSuite extends FunSuite {

  test("update string") {
    val dbProp = new Properties
    dbProp.load(new FileInputStream("db.properties"))
    KDB.setForUrl(dbProp.getProperty("dbUrl"), dbProp.getProperty("dbUser"), dbProp.getProperty("dbPassword"), dbProp.getProperty("dbDriver"))
    OwnerDAO updateOwnersGeocodes List((1.toLong,"XXX"),(1.toLong,"XXX"))
    assert(true)
  }

}