package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(@Column(length = 30) var name : String,
                @Column(length = 30) var lastname : String,
                @Column var email : String,
                @Column(length = 30) var address : String,
                @Column var password : String,
                @Column(length = 22) var cvu : String,
                @Column(length = 8) var walletAddress : String,
                @Column var points: Int,
                @Column var numberOfOperations: Int) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0
}