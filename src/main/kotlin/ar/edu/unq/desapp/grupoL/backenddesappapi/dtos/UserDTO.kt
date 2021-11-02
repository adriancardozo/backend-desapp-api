package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

data class UserDTO(val name : String,
                   val lastname : String,
                   val email : String,
                   val address : String,
                   val cvu : String,
                   val walletAddress : String,
                   val points: Int,
                   val numberOfOperations: Int,
                   val reputation: Float?)
