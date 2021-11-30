package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

data class ActivityUserDTO(val name: String,
                           val lastname: String,
                           val operations: Int,
                           val reputation: Float?,
                           val email: String
                           )
