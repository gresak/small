package sk.gresak.kataster

import java.text.SimpleDateFormat
import java.sql.Timestamp
import io.{Source, BufferedSource}
import java.util.Date

class OwnershipReport(path: String) {

  //val text: String = new PdfText(path).txt
  val source: BufferedSource = Source.fromFile("C:\\Users\\edo\\Documents\\Urbariat\\xxx.txt")
  val text: String = source.mkString
  //  val text: String = ""

  /*
    val source2: BufferedSource = Source.fromFile("C:\\Users\\edo\\Documents\\Urbariat\\zzz.txt")
    val boxes: String = source2.mkString
  */

  val actualizationRE = """Aktualizácia\skatastrálneho\sportálu:\s(\d\d\.\d\d\.\d\d\d\d)""".r
  val creationRE = """(?s)Dátum\svyhotovenia\s(\d\d\.\d\d\.\d\d\d\d).*?Čas\svyhotovenia:\s(\d\d\:\d\d\:\d\d)""".r
  val headerRE = """(?s)Por.\sčíslo\sPriezvisko.*?vlastníka.*?\n""".r
  val footerRE = """(?s)Informatívny\svýpis.*?\n""".r
  val partSplitRE = """(?s)\nČASŤ\s[A-K]:.*?\n""".r
  val ownerSplitRE = """(?s)\n(?=Účastník\správneho\svzťahu:.*\n\d+\s)""".r
  val ownerBoxRE = """(?s)Účastník\správneho\svzťahu:\s(.*?)\n(\d+)\s(.*?)\s*(\d+\s*/\s*\d+).*?\n(.*?)\n?Dátum\snarodenia\s:\s(\d\d\.\d\d\.\d\d\d\d)(.*)""".r

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

  def parseOwner(box: String): (String, Int, String, String, String, java.util.Date, String) =
    box match {
      case ownerBoxRE(participant, num, nameAddress, share, street, birthDateStr, entitlement) =>
        (participant, Integer.parseInt(num), nameAddress, share, street, new SimpleDateFormat("dd.MM.yyyy") parse birthDateStr, entitlement)
      case _ => throw new Exception("Owner box not parsed: " + box)
    }

  def processOwners(s: String) {
    val ownerBoxArray = ownerSplitRE.split(s)
    val owners: Array[(String, Int, String, String, String, Date, String)] = ownerBoxArray.map(parseOwner)
    owners foreach (s => {
      println(s._1)
      println(s._2)
      println(s._3)
      println(s._4)
      println(s._5)
      println(s._6)
      println(s._7)
    })
  }

  def process() {
    //storeReport()
    val s1 = headerRE.replaceAllIn(text, "")
    val s2 = footerRE.replaceAllIn(s1, "")
    val parts: Array[String] = partSplitRE.split(s2)
    processOwners(parts(2))
  }

}