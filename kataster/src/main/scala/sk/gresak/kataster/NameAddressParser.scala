package sk.gresak.kataster

object NameAddressParser {
  val partsRe = """([^\s,]+)\s([^\s,]+)[\s,]+([rR]O?D?\.[\s,]*([^\s,]+)[\s,]+)?(.*+)""".r
  val removeCommasRe = """\s*,\s*""".r
  val removeSRRe = """\s+SR(\s*|$)""".r
  val removeINGRe = """^ING[\s\.]+""".r
  val repairCSRe = """(?<=(\s{1}|^))[CČcč][Ss]?\.""".r
  val findCRRe = """(.*?)\s+ČR\s*""".r
  val zipRe = """(.*?)(\sPSČ\s)?(\d\d\d\s?\d\d)([\s,](.*)|$)""".r

  def parse(str: String): (String, String, String, String, String, String) = {
    str match {
      case partsRe(surname, name, born1, born2, rest0) => {
        val rest1 = removeCommasRe.replaceAllIn(rest0, " ")
        val rest2 = removeSRRe.replaceAllIn(rest1, "")
        val rest3 = removeINGRe.replaceAllIn(rest2, "")
        val rest4 = repairCSRe.replaceAllIn(rest3, "č.")
        val (zip0,rest5) = rest4 match {
          case zipRe(null, x, zipStr, y, r2) => (zipStr, r2)
          case zipRe(r1, x, zipStr, y, null) => (zipStr, r1)
          case zipRe(r1, x, zipStr, y, r2) => (zipStr, r1 + " " + r2)
          case _ => (null, rest4)
        }
        val rest = rest5.trim;
        val zip=if(zip0==null) null else zip0.replace(" ","")
        rest match {
          case findCRRe(resty) => (name, surname, born2, resty, zip, "CR")
          case _ => (name, surname, born2, rest, zip, null)
        }
      }
      case _ => throw new Exception("Nenaslo sa priezvisko v \n" + str + "\nvo formate: " + partsRe)
    }
  }


}