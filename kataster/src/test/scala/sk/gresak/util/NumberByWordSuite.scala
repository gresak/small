package sk.gresak.util

import org.scalatest.FunSuite

class NumberByWordSuite extends FunSuite {

  test("All") {
    assert(NumberByWords(741)==="sedemstoštyridsaťjeden")
    assert(NumberByWords(101012)==="stojedentisícdvanásť")
    assert(NumberByWords(1027)==="tisícdvadsaťsedem")
  }

}