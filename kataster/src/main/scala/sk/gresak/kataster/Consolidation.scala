package sk.gresak.kataster

import io.{BufferedSource, Source}
import sk.gresak.domain.ConsolidatedOwner
import collection.Iterator
import ConsolidationDAO._
import sk.gresak.util.Parse._

object Consolidation {

  def importCvs(path: String) {
    val source: BufferedSource = Source.fromFile(path)
    val lines = source.getLines()
    val parsedLines: Iterator[(ConsolidatedOwner, Seq[Int])] = lines map {
      line =>
        val item: Array[String] = line.split(";")
        val consolidatedOwner: ConsolidatedOwner = ConsolidatedOwner(
          int(item(0)), item(1), item(2), item(3), item(4), int(item(5)), int(item(6)),
          item(7), int(item(8)), int(item(9)), int(item(10)), item(12), int(item(13))
        )
        val additionalOwnerIds: Seq[Int] = if (item(11).size > 0) item(11).split(",").map(s => int(s)) else Seq[Int]()
        (consolidatedOwner, additionalOwnerIds)
    }
    val ar: Array[(ConsolidatedOwner, Seq[Int])] = parsedLines.toArray
    source.close()
    insertConsolidation(ar)
  }
}