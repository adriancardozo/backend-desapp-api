package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import javax.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction(@OneToOne(cascade = [CascadeType.PERSIST]) val activity: Activity,
                       @ManyToOne(cascade = [CascadeType.PERSIST]) val creator: User,
                       @ManyToOne(cascade = [CascadeType.PERSIST]) val activityCreator: User,
                       @Column val startMillis: Long,
                       @Column var state: TransactionState = TransactionState.PENDING_ACCEPT
) {
    init {
        activity.startTransaction()
    }

    fun accept(user: User) = state.accept(this, user)

    fun reject(user: User) = state.reject(this, user)

    fun transfer(user: User) = state.transfer(this, user)

    fun commit(user: User) = state.commit(this, user)

    fun cancel(user: User) = state.cancel(this, user)

    fun buyer(): User = if(activity.type == ActivityType.BUY) activityCreator else creator

    fun seller(): User = if(activity.type == ActivityType.SALE) activityCreator else creator

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
}