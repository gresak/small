package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import KDB._
import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.session.Database.threadLocalSession
object ByWordsDAO extends BaseDAO {

  def insertByWords(byWords: Seq[(Int, String)]) = withTransaction {
    import slovom._
    (int_cislo ~ tx_slovo).insertAll(byWords: _*)
  }

}