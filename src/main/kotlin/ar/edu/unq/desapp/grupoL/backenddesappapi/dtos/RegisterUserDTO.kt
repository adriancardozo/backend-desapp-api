package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

data class RegisterUserDTO(val name : String,
                           val lastname : String,
                           val email : String,
                           val address : String,
                           val password : String,
                           val cvu : String,
                           val walletAddress : String)
