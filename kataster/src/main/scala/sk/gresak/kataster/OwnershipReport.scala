package sk.gresak.kataster

import java.text.SimpleDateFormat
import java.sql.Timestamp
import sk.gresak.util.PdfText

class OwnershipReport(path: String) {
  val text: String = new PdfText(path).txt
  val actualizationRE = """Aktualizácia\skatastrálneho\sportálu:\s(\d\d\.\d\d\.\d\d\d\d)""".r
  val creationRE = """(?s)Dátum\svyhotovenia\s(\d\d\.\d\d\.\d\d\d\d).*?Čas\svyhotovenia:\s(\d\d\:\d\d\:\d\d)""".r

  def storeReport() {
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
    println("\nInserted id = " + id.get)
  }

  def process() {
    storeReport()
    val parts: Array[String] = text.split("""\nČASŤ\s[A-K]:.*""")
    println(parts.size)
  }

}