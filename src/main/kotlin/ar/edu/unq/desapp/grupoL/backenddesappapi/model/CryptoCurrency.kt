package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema
import javax.persistence.*

data class CryptoCurrency(var quotation: Quotation,
                          var quotationHour: Long) {
    constructor() : this(Quotation("",0.0), 0)
}