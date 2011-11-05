package sk.gresak.kataster

import org.scalatest.FunSuite
import sk.gresak.domain.Owner
import sk.gresak.util.Parse

class ParseOwnerSuite extends FunSuite {

  test("Dubak") {
    val ownerStr =
      "Účastník právneho vzťahu: Vlastník\r\n" +
        "49 ĎUBÁK ŠTEFAN C.430 120  / 19200\r\n" +
        "Dátum narodenia : 17.06.1929\r\n" +
        "Titul nadobudnutia OSVEČENIE VYHLÁSENIA O VYDRŽANÍ Č.NZ 266/01 -40/02"
    val owner: Owner = OwnerParser.parse(1, ownerStr)
    assert(owner === Owner(1, "Vlastník", 49, "ŠTEFAN", "ĎUBÁK", null, "č.430", null, null, 120, 19200,
      Parse date "17.06.1929", null, null, "OSVEČENIE VYHLÁSENIA O VYDRŽANÍ Č.NZ 266/01 -40/02", null))
  }

  test("Holly") {
    val ownerStr = "Účastník právneho vzťahu: Vlastník\r\n" +
      "80 HOLLÝ JOZEF ČS.928 NESLUŠA 40  / 19200\r\n" +
      "Dátum narodenia : 10.04.1945\r\n" +
      "PLOMBA VYZNAČENÁ \r\n" +
      "Poznámka O ZAČATÍ\r\n EXEKÚCIE \r\n" +
      "Dátum narodenia : 10.04.1945\r\n" +
      "Titul nadobudnutia OSVEČENIE"
    val owner: Owner = OwnerParser.parse(1, ownerStr)
    assert(owner === Owner(1, "Vlastník", 80, "JOZEF", "HOLLÝ", null, "č.928 NESLUŠA", null, null, 40, 19200,
      Parse date "10.04.1945", null,
      "PLOMBA VYZNAČENÁ ",
      "OSVEČENIE",
      "Poznámka O ZAČATÍ\r\n EXEKÚCIE "))
  }

  test("Jantosik") {
    val ownerStr = "Účastník právneho vzťahu: Vlastník\r\n" +
      "359 JANTOŠÍK STANISLAV NESLUŠA 791 153  / 60000\r\n" +
      "Dátum narodenia : 10.11.1958\r\n" +
      "Poznámka JUDR. FRANEK JÁN\r\n O ZAČATÍ EXEKÚCIE PREDAJOM NEHNUTEĽNOSTI A ZRIADENÍM EXEKUČNÉHO\r\n" +
      "Poznámka JUDR. KRASŇANOVÁ MÁRIA, SÚDNY EXEKÚTOR POVAŽSKÁ BYSTRICA -\r\n" +
      "Dátum narodenia : 10.11.1958\r\n" +
      "Titul nadobudnutia ROZHODNUTIE 667/91"
    val owner: Owner = OwnerParser.parse(1, ownerStr)
    assert(owner === Owner(1, "Vlastník", 359, "STANISLAV", "JANTOŠÍK", null, "NESLUŠA 791", null, null, 153, 60000,
      Parse date "10.11.1958", null, null, "ROZHODNUTIE 667/91",
      "Poznámka JUDR. FRANEK JÁN\r\n O ZAČATÍ EXEKÚCIE PREDAJOM NEHNUTEĽNOSTI A ZRIADENÍM EXEKUČNÉHO\r\n" +
        "Poznámka JUDR. KRASŇANOVÁ MÁRIA, SÚDNY EXEKÚTOR POVAŽSKÁ BYSTRICA -"))
  }

  test("Sulganova") {
    val ownerStr = "Účastník právneho vzťahu: Vlastník\r\n" +
      "288 ŠULGÁNOVÁ MÁRIA,NESLUŠA č.708 80  / 19200\r\n" +
      "Dátum narodenia : 19.09.1933"
    val owner: Owner = OwnerParser.parse(1, ownerStr)
    assert(owner === Owner(1, "Vlastník", 288, "MÁRIA", "ŠULGÁNOVÁ", null, "NESLUŠA č.708", null, null, 80, 19200,
      Parse date "19.09.1933", null, null, null, null))
  }

  test("Kacerikova") {
    val ownerStr = "Účastník právneho vzťahu: Vlastník\r\n" +
      "2 KÁCERÍKOVÁ LÝDIA ,KYSUCKÉ NOVÉ MESTO, UL.JESENSKÉHO 10  / 19200\r\n" +
      "1180/6\r\n" +
      "Dátum narodenia : 01.09.1953\r\n" +
      "Titul nadobudnutia OSVEČENIE"
    val owner: Owner = OwnerParser.parse(1, ownerStr)
    assert(owner === Owner(1, "Vlastník", 2, "LÝDIA", "KÁCERÍKOVÁ", null, "KYSUCKÉ NOVÉ MESTO UL.JESENSKÉHO 1180/6",
      null, null, 10, 19200, Parse date "01.09.1953", null, null, "OSVEČENIE", null))
  }

}