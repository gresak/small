package sk.gresak.kataster

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import KDB._
import sk.gresak.domain.ConsolidatedOwner
import collection.Seq

object ConsolidationDAO extends BaseDAO {
  def insertConsolidation(it: Array[(ConsolidatedOwner, Seq[Int])]) {
    import konsolidacia._
    withTransaction {
      it foreach {
        t =>
          val o: ConsolidatedOwner = t._1
          (int_por_cislo ~ tx_meno ~ tx_priezvisko ~ tx_adresa ~ tx_stat ~ int_podiel1 ~ int_podiel2 ~ tx_nadobudnutie ~
            int_zadrzat ~ int_hotovost ~ int_zomrel ~ tx_poznamka ~ int_koniec ).
            insert(ConsolidatedOwner.unapply(o).get)
          val seq: Seq[Int] = t._2
          if (seq.size > 0) {
            val id = insertedId.get
            val pairs = seq map ((id, _))
            (viac_podielov.id_konsolidacia ~ viac_podielov.int_por_cislo).insertAll(pairs: _*)
          }
      }
    }
  }

}