package sk.gresak.kataster

import sk.gresak.domain.Owner
import sk.gresak.util.Parse

object OwnerParser {

  val participantRe = """(?s)Účastník\správneho\svzťahu:\s(.*?)\r\n(\d+)\s(.*?)\s*(\d+)\s*/\s*(\d+)\r\n(.*)""".r
  val birthIcoRe = """(?s)(Dátum\snarodenia|IČO)\s:\s(\d\d\.\d\d\.\d\d\d\d|.*?)(\r\n|$).*""".r
  val birthIcoReplaceRe = """(?s)(Dátum\snarodenia|IČO)\s:\s(\d\d\.\d\d\.\d\d\d\d|.*?)(\r\n|$)""".r
  val entitlementRe = """(?s)(.*?)Titul\snadobudnutia\s(.*)""".r
  val plombRe = """(?s)(.*?)(PLOMBA\s.*?)\r\n(.*)""".r
  val notesRe = """(?s)(.*?)(Poznámka.*?)\r\n""".r
  val endReplaceRe = """(?s)\r\n""".r

  def parse(idReport: Long, box: String): Owner = {
    val (participant, num, nameAddress, share1, share2, box1) = box match {
      case participantRe(participantStr, numStr, nameAddressStr, share1Str, share2Str, box1Str) =>
        (participantStr, Integer.parseInt(numStr), nameAddressStr, Integer.parseInt(share1Str), Integer.parseInt(share2Str), box1Str)
      case _ => throw new Exception("\nbox not parsed:\n" + box)
    }
    val (labelStr, str) = birthIcoRe findFirstMatchIn box1 match {
      case Some(y) => (y.subgroups(0), y.subgroups(1))
      case _ => throw new Exception("Nenasiel sa datum narodenia alebo ico v \n" + box1 + "\nvo formate: " + birthIcoRe)
    }
    val (birthDate, ico) =
      if ("IČO" == labelStr) (null, str)
      else (Parse date str, null)
    val x = birthIcoReplaceRe.replaceAllIn(box1, "")
    val (rest, entitlement) = x match {
      case entitlementRe(restStr, entitlementStr) => (restStr, entitlementStr)
      case _ => (x, null)
    }
    val (rest1, plomb) = rest match {
      case plombRe(restStr1, plombStr, restStr2) => (restStr1 + restStr2, plombStr)
      case _ => (rest, null)
    }
    val (street2, notes) = rest1 match {
      case notesRe(street1, notesx) => (street1, notesx)
      case _ => (rest1, null)
    }
    val street3 = endReplaceRe.replaceAllIn(street2, "")
    val street = if (street3 != "") " " + street3 else ""
    val addressParts = NameAddressParser.parse(nameAddress + street)
    Owner(idReport, participant, num,
      addressParts._1, addressParts._2, addressParts._3, addressParts._4, addressParts._5, addressParts._6,
      share1, share2, birthDate, ico, plomb, entitlement, notes)
  }

}