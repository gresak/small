package sk.gresak.kataster

import sk.gresak.util.NumberByWords

object RunInsertByWords {
  def main(args: Array[String]) {
    KDB.setDb(args(0))
    ByWordsDAO.insertByWords((1 to 10000).map {
      i =>
        val x = NumberByWords(i)
        (i, x.head.toUpper + x.tail)
    })
  }
}


