package sk.gresak.kataster

import sk.gresak.util.PdfText
import java.text.SimpleDateFormat
import io.{BufferedSource, Source}

class OwnershipReport(path: String) {
  val source: BufferedSource = Source.fromFile("C:\\Users\\edo\\Documents\\Urbariat\\xxx.txt")
  val text: String = source.mkString
  //val text: String = "\naaaaa\nČASŤ A:x\nggg"
  //val text: String = new PdfText(path) txt
  val actualizationRE = """Aktualizácia\skatastrálneho\sportálu:\s(\d\d\.\d\d\.\d\d\d\d)""".r

  def storeReport() {
    val dateStr = actualizationRE findFirstMatchIn text match {
      case Some(x) => x.subgroups(0)
      case _ => throw new Exception("Nenasiel sa datum aktualizacie portalu vo formate: " + actualizationRE)
    }
    val dateOfActualization = new SimpleDateFormat("dd.MM.yyyy") parse dateStr
    val sqlDate = new java.sql.Date(dateOfActualization.getTime)
    val id = new LoadDAO insertRawReport(path, sqlDate, text)
    println("\nInserted id = " + id.get)
  }

  def process() {
    //storeReport
    val parts: Array[String] = text.split("""\nČASŤ\s[A-K]:.*""")
    println(parts.size)
  }

}