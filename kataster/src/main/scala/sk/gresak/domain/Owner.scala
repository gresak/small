package sk.gresak.domain

case class Owner(idReport: Long, participant: String, num: Int, nameAddress: String, share: String,
                 birthDate: java.sql.Date, ico: String, plomba: String, entitlement: String)