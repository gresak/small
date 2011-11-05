package sk.gresak.domain

case class Owner(idReport: Long, participant: String, num: Int,
                 name: String, surname: String, born: String, address: String, zip: String, state: String,
                 share1: Int, share2: Int, birthDate: java.sql.Date, ico: String,
                 plomba: String, entitlement: String, notes: String)