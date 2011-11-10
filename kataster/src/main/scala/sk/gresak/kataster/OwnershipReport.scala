package sk.gresak.kataster

import java.text.SimpleDateFormat
import sk.gresak.domain.Owner
import sk.gresak.util.PdfText
import io.{Source, BufferedSource}
import java.sql.{Timestamp, Date}
import util.matching.Regex

class OwnershipReport(reportPath: String, replacementsPath: String) {

  lazy val text: String = new PdfText(reportPath).txt
  lazy val source: BufferedSource = Source.fromFile("C:\\Users\\edo\\Documents\\Urbariat\\xxx.txt")
  lazy val textTest: String = source.mkString
  lazy val replacements: List[(Regex, String)] = loadReplacements(replacementsPath)

  val actualizationRE = """Aktualizácia\skatastrálneho\sportálu:\s(\d\d\.\d\d\.\d\d\d\d)""".r
  val creationRE = """(?s)Dátum\svyhotovenia\s(\d\d\.\d\d\.\d\d\d\d).*?Čas\svyhotovenia:\s(\d\d\:\d\d\:\d\d)""".r
  val headerRE = """(?s)Por.\sčíslo\sPriezvisko.*?vlastníka.*?\n""".r
  val footerRE = """(?s)Informatívny\svýpis.*?\n""".r
  val partSplitRE = """(?s)\r\nČASŤ\s[A-K]:.*?\n""".r
  val ownerSplitRE = """(?s)\r\n(?=Účastník\správneho\svzťahu:.*\r\n\d+\s)""".r

  def loadReplacements(path: String) = Source.fromFile(path).getLines().map(s => {
    s.split("""::""") match {
      case Array(x, y) => (new Regex(x), y)
      case Array(x) => (new Regex(x), "")
      case _ => throw new Exception("Error in replacements file: " + path)
    }
  }
  ).toList

  private def extractDates: (Timestamp, Date) = {
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
    val sqlCreated = new java.sql.Timestamp(created.getTime)
    (sqlCreated, sqlActualized)
  }

  def storeReport(): Long = {
    val r = extractDates
    val sqlCreated: Timestamp = r._1
    val sqlActualized: Date = r._2
    val id = new LoadDAO insertRawReport(reportPath, sqlActualized, sqlCreated, text)
    println("\nInserted id = " + id)
    id
  }

  def processOwners(idReport: Long, s: String): Array[Owner] = {
    val ownerParser: OwnerParser = new OwnerParser(replacements)
    ownerSplitRE.split(s).map(ownerParser.parse(idReport, _))
  }

  def process() {
    val idReport: Long = storeReport()
    val x = headerRE.replaceAllIn(text, "")
    val y = footerRE.replaceAllIn(x, "")
    val parts: Array[String] = partSplitRE.split(y)
    val owners: Array[Owner] = processOwners(idReport, parts(2))
    OwnerDAO.insertOwners(owners)
  }

}