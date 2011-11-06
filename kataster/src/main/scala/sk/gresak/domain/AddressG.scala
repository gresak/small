package sk.gresak.domain

case class ResponseG(results: List[AddressG], status: String)

case class AddressG(address_components: List[AddressComponentG], formatted_address: String, types: List[String])

case class AddressComponentG(long_name: String, short_name: String, types: List[String])