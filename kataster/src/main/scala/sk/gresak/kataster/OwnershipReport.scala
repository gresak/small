package sk.gresak.kataster

import java.text.SimpleDateFormat
import sk.gresak.domain.Owner
import java.sql.{Date, Timestamp}
import sk.gresak.util.PdfText
import io.{Source, BufferedSource}

class OwnershipReport(path: String) {

  val textReal: String = new PdfText(path).txt
  val source: BufferedSource = Source.fromFile("C:\\Users\\edo\\Documents\\Urbariat\\xxx.txt")
  val text: String = source.mkString

  val actualizationRE = """Aktualizácia\skatastrálneho\sportálu:\s(\d\d\.\d\d\.\d\d\d\d)""".r
  val creationRE = """(?s)Dátum\svyhotovenia\s(\d\d\.\d\d\.\d\d\d\d).*?Čas\svyhotovenia:\s(\d\d\:\d\d\:\d\d)""".r
  val headerRE = """(?s)Por.\sčíslo\sPriezvisko.*?vlastníka.*?\n""".r
  val footerRE = """(?s)Informatívny\svýpis.*?\n""".r
  val partSplitRE = """(?s)\nČASŤ\s[A-K]:.*?\n""".r
  val ownerSplitRE = """(?s)\n(?=Účastník\správneho\svzťahu:.*\n\d+\s)""".r
  val ownerBoxRE = """(?s)Účastník\správneho\svzťahu:\s(.*?)\n(\d+)\s(.*?)\s*(\d+\s*/\s*\d+).*?\n(.*?)\n?(Dátum\snarodenia|IČO)\s:\s(\d\d\.\d\d\.\d\d\d\d|.*?).*?(\n|)(.*)""".r

  def storeReport(): Long = {
    val actualizedStr = actualizationRE findFirstMatchIn text match {
      case Some(x) => x.subgroups(0)
      case _ => throw new Exception("Nenasiel sa datum aktualizacie portalu vo formate: " + actualizationRE)
    }
    val actualized = new SimpleDateFormat("dd.MM.yyyy") parse actualizedStr
    val sqlActualized = new java.sql.Date(actualized.getTime)
    val createdStr = creationRE findFirstMatchIn text match {
      case Some(x) => x.subgroups(0) + " " + x.subgroups(1)
      case _ => throw new Exception("Nenasiel sa cas vyhotovenia vo formate: " + creationRE)
    }
    val created = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss") parse createdStr
    val sqlCreated = new Timestamp(created.getTime)
    val id = new LoadDAO insertRawReport(path, sqlActualized, sqlCreated, text)
    println("\nInserted id = " + id)
    id
  }

  def parseOwner(idReport: Long, box: String): Owner =
    box match {
      case ownerBoxRE(participant, num, nameAddress, share, street, labelStr, str, y, entitlement) =>
        val (birthDate, ico) =
          if (labelStr == "IČO") (null, str)
          else {
            val d: java.util.Date = new SimpleDateFormat("dd.MM.yyyy") parse str
            (new Date(d.getTime), "")
          }
        Owner(idReport, participant, Integer.parseInt(num), nameAddress + " " + street, share, birthDate, ico, entitlement)
      case _ => throw new Exception("\nOwner box not parsed:\n" + box)
    }

  def processOwners(idReport: Long, s: String): Array[Owner] = ownerSplitRE.split(s).map(parseOwner(idReport, _))

  def process() {
    //val idReport: Long = storeReport()
    val x = headerRE.replaceAllIn(text, "")
    val y = footerRE.replaceAllIn(x, "")
    val parts: Array[String] = partSplitRE.split(y)
    val owners: Array[Owner] = processOwners(1, parts(2))
    //val owners: Array[Owner] = processOwners(idReport, parts(2))
    val ownerDAO: OwnerDAO = new OwnerDAO
    ownerDAO.insertOwners(owners)
  }

}