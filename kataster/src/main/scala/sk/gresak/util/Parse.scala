package sk.gresak.util

import java.text.SimpleDateFormat

object Parse {
  def date(dateStr: String): java.sql.Date = {
    new java.sql.Date(new SimpleDateFormat("dd.MM.yyyy") parse dateStr getTime)
  }

  def int(intStr: String): Int = if (intStr.size > 0) intStr.toInt else 0

}