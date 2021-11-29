package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema
import javax.persistence.*

@Entity
@Table(name = "quotations")
@JsonSerializableSchema
data class Quotation(@Column var name: String,
                     @Column var arPrice: Double) {
    constructor() : this("",0.0)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0
}
