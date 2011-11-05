package sk.gresak.kataster

import org.scalatest.FunSuite

class ParseNameAddressSuite extends FunSuite {
  test("Dubak") {
    val nameAddressStr = "ĎUBÁK ŠTEFAN C.430"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("ŠTEFAN", "ĎUBÁK", null, "č.430", null,null))
  }
  test("Plevkova") {
    val nameAddressStr = "PLEVKOVÁ ANNA R.BIELA ČS.471 NESLUŠA"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("ANNA", "PLEVKOVÁ", "BIELA", "č.471 NESLUŠA", null,null))
  }
  test("Surhanakova") {
    val nameAddressStr = "ŠURHAŇÁKOVÁ ANNA r. MINDEKOVÁ, č. 387, SR"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("ANNA", "ŠURHAŇÁKOVÁ", "MINDEKOVÁ", "č. 387", null,null))
  }
  test("Gazdik") {
    val nameAddressStr = "GAZDÍK LADISLAV r. GAZDÍK, ING., KAŠTANOVÁ 1415, ŠENOV, ČR"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("LADISLAV", "GAZDÍK", "GAZDÍK", "KAŠTANOVÁ 1415 ŠENOV", null,"CR"))
  }
  test("Lavicskova") {
    val nameAddressStr = "LAVICSKOVÁ EVA ROD.ŠUTÁKOVÁ ČS.300 NESLUŠA"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("EVA", "LAVICSKOVÁ", "ŠUTÁKOVÁ", "č.300 NESLUŠA", null, null))
  }
  test("Strbova") {
    val nameAddressStr = "Štrbová Mária r. Koptáková, DL.POĽSKÉHO 603/14, KYSUCKÉ NOVÉ MESTO, PSČ 024 01, SR"
    val n = NameAddressParser.parse(nameAddressStr)
    assert(n ===("Mária", "Štrbová", "Koptáková", "DL.POĽSKÉHO 603/14 KYSUCKÉ NOVÉ MESTO", "02401",null))
  }
}