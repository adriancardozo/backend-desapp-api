package ar.edu.unq.desapp.grupoL.backenddesappapi.model

data class User(var name : String,
                var lastname : String,
                var email : String,
                var address : String,
                var password : String,
                var cvu : String,
                var walletAddress : String )