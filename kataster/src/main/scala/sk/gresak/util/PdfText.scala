package sk.gresak.util

import java.net.URL
import org.apache.pdfbox.util.PDFTextStripper
import org.apache.pdfbox.pdmodel.PDDocument
import java.io._
import io.{BufferedSource, Source}

class PdfText(fileName: String) {
  val lines: BufferedSource = {
    val document = PDDocument.load(new URL("""file:///""" + fileName), true)
    try {
      val stripper = new PDFTextStripper("UTF-8")
      stripper.setSortByPosition(true)
      val fileNameTxt = fileName + ".txt"
      val outputFileStr = (new File(fileNameTxt)).getAbsolutePath
      val utf8 = "UTF-8"
      val outputWriter = new OutputStreamWriter(new FileOutputStream(outputFileStr), utf8)
      stripper.writeText(document, outputWriter);
      outputWriter.close()
      Source.fromURL(new URL("""file:///""" + fileName + ".txt"), utf8)
    } finally {
      document.close()
    }
  }

}

