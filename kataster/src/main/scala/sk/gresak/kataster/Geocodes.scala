package sk.gresak.kataster

import dispatch._
import OwnerDAO._

object Geocodes {

  val params1: String = "json?region=sk&language=sk&sensor=false&address="
  val reqPart1: Request = :/("maps.googleapis.com") / ("maps/api/geocode/")

  def reload(id_report: Long) = {
    val ownersGeocodes: List[(Long, String)] = readOwners(id_report) map {
      s =>
        val request: Request = reqPart1 / (params1 + s._2)
        (s._1, Http(request as_str))
    }
   // updateOwnersGeocodes(ownersGeocodes)
  }
}