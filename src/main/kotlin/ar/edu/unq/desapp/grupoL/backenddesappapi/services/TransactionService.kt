package ar.edu.unq.desapp.grupoL.backenddesappapi.services

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.*
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.InvalidOperationException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.TransactionNotFoundException
import ar.edu.unq.desapp.grupoL.backenddesappapi.model.*
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.TransactionRepository
import ar.edu.unq.desapp.grupoL.backenddesappapi.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService {
    @Autowired
    private lateinit var repository: TransactionRepository
    @Autowired
    private lateinit var activityService: ActivityService
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var jwtUserService: JwtUserService

    @Transactional
    fun createTransaction(token: String, createTransaction: CreateTransactionDTO): TransactionDTO {
        val activity: Activity = activityService.findById(createTransaction.activityId)
        val user = getUser(token)
        checkNotIsUserActivity(activity, user)
        val transaction = repository.save(Transaction(activity, user, activity.user, System.currentTimeMillis()))
        return toTransactionDTO(transaction, user)
    }

    fun transaction(token: String, id: Long): TransactionDTO {
        val user = getUser(token)
        return toTransactionDTO(getUserTransaction(user, id), user)
    }

    fun userTransactions(token: String): List<TransactionDTO> {
        val user = getUser(token)
        return user.transactions().reversed().map { toTransactionDTO(it, user) }
    }

    @Transactional
    fun accept(token: String, id: Long) {
        val user = getUser(token)
        val transaction = getTransaction(id)
        transaction.accept(user)
        repository.save(transaction)
    }

    @Transactional
    fun reject(token: String, id: Long) {
        val user = getUser(token)
        val transaction = getTransaction(id)
        transaction.reject(user)
        repository.save(transaction)
    }

    @Transactional
    fun transfer(token: String, id: Long) {
        val user = getUser(token)
        val transaction = getTransaction(id)
        transaction.transfer(user)
        repository.save(transaction)
    }

    @Transactional
    fun commit(token: String, id: Long) {
        val user = getUser(token)
        val transaction = getTransaction(id)
        transaction.commit(user)
        repository.save(transaction)
    }

    @Transactional
    fun cancel(token: String, id: Long) {
        val user = getUser(token)
        val transaction = getTransaction(id)
        transaction.cancel(user)
        repository.save(transaction)
    }

    private fun getUserTransaction(user: User, id: Long) =
        user.transactions().find { it.id == id } ?: throw TransactionNotFoundException()

    private fun getTransaction(id: Long): Transaction {
        val transaction = repository.findById(id)
        if(transaction.isEmpty) throw TransactionNotFoundException()
        return transaction.get()
    }

    private fun getUser(token: String) = userRepository.findByEmail(jwtUserService.userEmail(token))

    private fun toTransactionDTO(transaction: Transaction, user: User): TransactionDTO {
        return if(isSeller(transaction, user)) {
            toSellerTransactionDTO(transaction)
        } else {
            toBuyerTransactionDTO(transaction)
        }
    }

    private fun toBuyerTransactionDTO(transaction: Transaction) =
        BuyerTransactionDTO(
            transaction.id,
            toActivityDTO(transaction.activity),
            SellerDTO(transaction.seller().cvu),
            transaction.state
        )

    private fun toSellerTransactionDTO(transaction: Transaction) =
        SellerTransactionDTO(
            transaction.id,
            toActivityDTO(transaction.activity),
            BuyerDTO(transaction.buyer().walletAddress),
            transaction.state
        )

    private fun toActivityDTO(activity: Activity) = ActivityDTO(
        activity.id,
        activity.hour.toString(),
        ActivityQuotationDTO(activity.quotation.name, activity.quotation.arPrice),
        activity.amount,
        activity.amount * activity.quotation.arPrice,
        ActivityUserDTO(
            activity.user.name,
            activity.user.lastname,
            activity.user.numberOfOperations,
            activity.user.reputation(),
            activity.user.email
        ),
        activity.type
    )

    private fun isSeller(transaction: Transaction, user: User) =
        transaction.seller().id == user.id
        //(transaction.activity.user.id != user.id) == (transaction.activity.type == ActivityType.BUY)

    private fun checkNotIsUserActivity(activity: Activity, user: User) {
        if(user.id == activity.user.id) throw InvalidOperationException()
    }
}