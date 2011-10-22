package sk.gresak.kataster

import java.io.FileInputStream
import java.util.Properties
import sk.gresak.util.PdfText

object RunKataster {
  def main(args: Array[String]) {
    val ps = new Properties
    ps.load(new FileInputStream(args(0)))
    KDB.setForUrl(ps.getProperty("dbUrl"), ps.getProperty("dbUser"), ps.getProperty("dbPassword"), ps.getProperty("dbDriver"))
    val pdfReportPath = ps.getProperty("zostava")
    val reportText = new PdfText(pdfReportPath)
    val id = (new LoadDAO()).insertRawReport(pdfReportPath, reportText.txt)
    println("\nInserted id = "+id.get)
  }

}


