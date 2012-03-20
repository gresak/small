package sk.gresak.domain

case class ConsolidatedOwner(num: Int,
                             name: String, surname: String, address: String, state: String,
                             share1: Int, share2: Int, entitlement: String,
                             keep: Int, cash: Int, passedAway: Int, note: String, end: Int )