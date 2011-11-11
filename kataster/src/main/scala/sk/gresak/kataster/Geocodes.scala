package sk.gresak.kataster

import dispatch._
import OwnerDAO._
import net.liftweb.json._
import sk.gresak.domain.{ResponseG, AddressG}

object Geocodes {

  val params1: String = "json?bounds=49.20414,18.499603|49.537687,18.935623&region=sk&language=sk&sensor=false&address="
  val reqPart1: Request = :/("maps.googleapis.com") / ("maps/api/geocode/")

  def reloadByIdReport(id_report: Long) = {
    val ownersGeocodes: List[(Long, String)] = readOwners(id_report) map {
      s =>
        val request: Request = reqPart1 / (params1 + Request.encode_%(s._2))
        Thread.sleep(1000)
        (s._1, Http(request as_str))
    }
    updateOwnersGeocodes(ownersGeocodes)
  }

  def updateFormattedAddress(id_report: Long) = {
    val geocodes = readGeocodes(id_report)
    implicit val formats = DefaultFormats
    val addresses = geocodes.map {
      r =>
        val res: List[AddressG] = r._2.results
        (r._1, if (res.size == 1) res(0).formatted_address else null)
    }
    updateOwnersFormattedAddresses(addresses)
  }

  def selectAddressG(g: ResponseG) = {
    val results: List[AddressG] = g.results
    results.length match {
      case 0 => throw new Exception("Nonempty results expeted in selectAddressG")
      case 1 => results(0)
      case i => {
        val skResults = results.toStream.filter {
          adr =>
            adr.address_components.filter({
              comp => comp.types(0) == "country" && comp.short_name == "SK"
            }).isEmpty != true
        }
        if (skResults.isEmpty) results(0) else skResults.head
      }
    }
  }

  def updateAddressFromGeocodes(id_report: Long) = {
    val geocodes = readGeocodes(id_report)
    implicit val formats = DefaultFormats
    val addresses = geocodes.map {
      case (idOwner, response) => if (response.results.length == 0) (idOwner, null, null, null, 0)
      else {
        val addressG: AddressG = selectAddressG(response)
        val formatted: String = addressG.formatted_address
        val localities = addressG.address_components.filter(comp => comp.types(0) == "locality")
        val postalCodes = addressG.address_components.filter(comp => comp.types(0) == "postal_code")
        (idOwner, formatted,
          if (localities.isEmpty) null else localities(0).short_name,
          if (postalCodes.isEmpty) null else postalCodes(0).short_name,
          response.results.length)
      }
    }
    updateOwnersFormattedAddresses(addresses)
  }
}