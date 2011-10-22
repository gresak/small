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
    } finally {
      document.close()
    }
  }

}

