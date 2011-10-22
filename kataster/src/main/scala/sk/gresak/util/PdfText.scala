package sk.gresak.util

import java.net.URL
import org.apache.pdfbox.util.PDFTextStripper
import org.apache.pdfbox.pdmodel.PDDocument
class PdfText(fileName: String) {
  val txt: String = {
    val document = PDDocument.load(new URL("""file:///""" + fileName), true)
    try {
      val stripper = new PDFTextStripper("UTF-8")
      stripper.setSortByPosition(true)
      stripper.getText(document)
      /*
      val fileNameTxt = fileName + ".txt"
      val outputFileStr = (new File(fileNameTxt)).getAbsolutePath
      val utf8 = "UTF-8"
      val outputWriter = new OutputStreamWriter(new FileOutputStream(outputFileStr), utf8)
      stripper.writeText(document, outputWriter);
      outputWriter.close()
      Source.fromURL(new URL("""file:///""" + fileName + ".txt"), utf8).mkString
      */
    } finally {
      document.close()
    }
  }

}

