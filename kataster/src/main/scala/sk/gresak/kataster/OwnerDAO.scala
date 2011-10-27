package sk.gresak.kataster

import KDB._
import java.sql.Date
import sk.gresak.domain.Owner

class OwnerDAO {

  def insertOwners(owners: Array[Owner]): Unit = withSession {
    val tuples: Array[(Long, String, Int, String, String, Date, String, String, String)] = owners map (o => {
      val t: (Long, String, Int, String, String, Date, String, String, String) = Owner.unapply(o).get
      println("id_report = " + t._1)
      println("tx_pravny_vztah = " + t._2)
      println("int_por_cislo = " + t._3)
      println("tx_meno_adresa = " + t._4)
      println("tx_podiel = " + t._5)
      println("dt_narodenie = " + t._6)
      println("tx_ico = " + t._7)
      println("tx_plomba = " + t._8)
      println("tx_nadobudnutie = " + t._9)
      println("____________________________________________________________________________________")
      t
    })
    println("SIZE = " + tuples.size)
    //import vlastnik._
    //(id_report ~ tx_pravny_vztah ~ int_por_cislo ~ tx_meno_adresa ~ tx_podiel ~ dt_narodenie ~ tx_ico ~ tx_nadobudnutie).insertAll(tuples: _*)
  }


}