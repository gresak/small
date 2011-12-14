package sk.gresak.kataster

import org.scalatest.FunSuite
import net.liftweb.json._
import java.io.InputStreamReader
import sk.gresak.domain.ResponseG
import io.{BufferedSource, Source}

class ParseGeocodeResponseSuite extends FunSuite {

  implicit val formats = DefaultFormats

  def extr(s: String): ResponseG = {
    val src: BufferedSource = Source.fromFile(s)
    val r: InputStreamReader = src.reader()
    val v: JValue = JsonParser.parse(r)
    src.close()
    v.extract[ResponseG]
  }

  test("homovci") {
    val response = extr("homovci.json")
    assert(response.results(0).formatted_address === "Štúrova 133/8, 05801 Poprad, Slovenská republika")
  }

  test("babka") {
    val response = extr("babka.json")
    val address = response.results(0)
    assert(address.formatted_address === "02341 Nesluša, Slovenská republika")
    assert(address.address_components.length === 5)
    assert(address.address_components(0).types(1) === "political")
  }

}