package sk.gresak.kataster

import org.scalatest.FunSuite
import net.liftweb.json._
import io.Source
import java.io.InputStreamReader
import sk.gresak.domain.ResponseG

class ParseGeocodeResponseSuite extends FunSuite {

  implicit val formats = DefaultFormats

  test("homovci") {
    val r: InputStreamReader = Source.fromFile("homovci.json", "UTF-8").reader()
    val v: JValue = JsonParser.parse(r)
    val response: ResponseG = v.extract[ResponseG]
    assert(response.results(0).formatted_address === "Štúrova 133/8, 05801 Poprad, Slovenská republika")
  }

  test("babka") {
    val response = JsonParser.parse(Source.fromFile("babka.json").reader()).extract[ResponseG]
    val address = response.results(0)
    assert(address.formatted_address === "02341 Nesluša, Slovenská republika")
    assert(address.address_components.length === 5)
    assert(address.address_components(0).types(1) === "political")
  }

}