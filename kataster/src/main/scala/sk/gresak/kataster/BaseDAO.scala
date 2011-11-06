package sk.gresak.kataster

import org.scalaquery.ql.{Query, SimpleFunction}
import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession

trait BaseDAO {

  def insertedId: Some[Long] = {
    val insertedIdFunction = SimpleFunction.nullary[Long]("last_insert_id")
    new Some(Query(insertedIdFunction).first)
  }


}