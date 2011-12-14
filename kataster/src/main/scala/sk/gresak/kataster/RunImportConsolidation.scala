package sk.gresak.kataster

import io.{Source, BufferedSource}

object RunImportConsolidation {

  def main(args: Array[String]) {
    KDB.setDb(args(0))
    Consolidation.importCvs(args(1))
  }
}


