package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema

@JsonSerializableSchema
data class CryptoCurrency(var name: String,
                          var arPrice: Double,
                          var quotationHour: Long,
                          var id: String = "") {
    constructor() : this("",0.0, 0, "")
}