package sk.gresak.util

object NumberByWords {

  val digits = Array("", "jeden", "dve", "tri", "štyri", "päť", "šesť", "sedem", "osem", "deväť")
  val digits2 = Array("", "jeden", "dva", "tri", "štyri", "päť", "šesť", "sedem", "osem", "deväť")
  val g = Array("", "desiat", "sto", "tisíc")
  val g20 = "dsať"
  val g50 = "desiat"
  val ex = Map(2 -> "dva", 10 -> "desať", 11 -> "jedenásť", 12 -> "dvanásť", 14 -> "štrnásť", 15 -> "pätnásť", 16 -> "šestnásť", 19 -> "devätnásť")

  def write1(num: Int) = {
    val sList = digitList(num, List())._2

    sList.foldLeft("") {
      (s, k) => s + digits(k)
    }
  }

  def digitList(num: Int, dList: List[Int]): (Int, List[Int]) = {
    if (num > 9) {
      val d = num % 10
      digitList((num - d) / 10, d :: dList)
    } else
      (0, num :: dList)
  }

  def apply(num: Int): String = {
    if (ex.isDefinedAt(num)) ex.get(num).get
    else
    if (num < 10) digits(num)
    else {
      val d = digitList(num, List())._2.reverse
      if (num < 20) digits(d(0)) + "násť"
      else
      if (num < 30) "dvadsať" + digits2(d(0))
      else
      if (num < 50) digits(d(1)) + g20 + digits2(d(0))
      else
      if (num < 100) digits(d(1)) + g50 + digits2(d(0))
      else
      if (num < 200) "sto" + apply(num - 100)
      else
      if (num < 1000) digits(d(2)) + "sto" + apply(num % 100)
      else
      if (num < 2000) "tisíc" + apply(num % 1000)
      else
      if (num < 1000000) apply(num / 1000.toInt) + "tisíc" + apply(num % 1000)
      else
        throw new Exception("Number by word not defined for " + num)

    }

  }
}