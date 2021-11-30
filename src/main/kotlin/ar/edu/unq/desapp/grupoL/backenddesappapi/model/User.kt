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
                @Column var points: Int = 0,
                @Column var numberOfOperations: Int = 0,
                @OneToMany(mappedBy = "creator") var transactions: MutableList<Transaction> = emptyList<Transaction>().toMutableList(),
                @OneToMany(mappedBy = "activityCreator") var ownTransactions: MutableList<Transaction> = emptyList<Transaction>().toMutableList()
                //@OneToMany var transactions: MutableList<Transaction> = emptyList<Transaction>().toMutableList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0

    fun reputation(): Float? = if(numberOfOperations != 0) points / numberOfOperations.toFloat() else null

    fun transactions(): List<Transaction> = ownTransactions.plus(transactions)

    fun cancelTransaction() {
        points -= 20
    }

    fun fastCompleteTransaction() {
        numberOfOperations += 1
        points += 10
    }

    fun slowCompleteTransaction() {
        numberOfOperations += 1
        points += 5
    }
    //fun addTransaction(transaction: Transaction) = transactions.add(transaction)
}