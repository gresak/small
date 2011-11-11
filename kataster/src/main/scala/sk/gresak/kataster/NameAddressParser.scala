package sk.gresak.kataster

object NameAddressParser {

  val partsRe = """([^\s]+)\s([^\s]+)[\s]+([rR]O?D?\.[\s]*([^\s]+)[\s\.]+)?(.*+)""".r
  val zipRe = """(.*?)(\sPSČ\s)?(\d\d\d\s?\d\d)([\s,](.*)|$)""".r

  def parse(str: String): (String, String, String, String, String) = {
    str match {
      case partsRe(surname, name, born1, born, rest0) => {
        val (zip0,rest) = rest0 match {
          case zipRe(null, x, zipStr, y, r2) => (zipStr, r2)
          case zipRe(r1, x, zipStr, y, null) => (zipStr, r1)
          case zipRe(r1, x, zipStr, y, r2) => (zipStr, r1 + " " + r2)
          case _ => (null, rest0)
        }
        val zip=if(zip0==null) null else zip0.replace(" ","")
        (name, surname, born, rest.trim(), zip)
      }
      case _ => throw new Exception("Nenaslo sa priezvisko v \n" + str + "\nvo formate: " + partsRe)
    }
  }


}