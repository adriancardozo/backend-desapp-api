package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.InvalidOperationException

enum class TransactionState {
    PENDING_ACCEPT {
        override fun accept(transaction: Transaction, user: User) {
            if(transaction.activityCreator.id != user.id) throw InvalidOperationException()
            transaction.state = PENDING_TRANSFER
        }

        override fun reject(transaction: Transaction, user: User) {
            if(transaction.activityCreator.id != user.id) throw InvalidOperationException()
            transaction.state = REJECTED
        }
    },
    PENDING_TRANSFER{
        override fun transfer(transaction: Transaction, user: User) {
            if(transaction.buyer().id != user.id) throw InvalidOperationException()
            transaction.state = PENDING_COMMIT
        }
    },
    PENDING_COMMIT{
        override fun commit(transaction: Transaction, user: User) {
            if(transaction.seller().id != user.id) throw InvalidOperationException()
            val commitMillis: Long = System.currentTimeMillis()
            if(completedFast(commitMillis, transaction)){
                transaction.activityCreator.fastCompleteTransaction()
                transaction.creator.fastCompleteTransaction()
            } else {
                transaction.activityCreator.slowCompleteTransaction()
                transaction.creator.slowCompleteTransaction()
            }
            transaction.state = COMPLETED
        }

        private fun completedFast(commitMillis: Long, transaction: Transaction) =
            commitMillis - transaction.startMillis <= 1800000
    },
    COMPLETED{
        override fun cancel(transaction: Transaction, user: User): Unit = throw InvalidOperationException()
    },
    REJECTED{
        override fun cancel(transaction: Transaction, user: User): Unit = throw InvalidOperationException()
    },
    CANCELED{
        override fun cancel(transaction: Transaction, user: User): Unit = throw InvalidOperationException()
    };

    open fun accept(transaction: Transaction, user: User): Unit = throw InvalidOperationException()

    open fun reject(transaction: Transaction, user: User): Unit = throw InvalidOperationException()

    open fun transfer(transaction: Transaction, user: User): Unit = throw InvalidOperationException()

    open fun commit(transaction: Transaction, user: User): Unit = throw InvalidOperationException()

    open fun cancel(transaction: Transaction, user: User) {
        if(transaction.seller().id != user.id && transaction.buyer().id != user.id) throw InvalidOperationException()
        user.cancelTransaction()
        transaction.state = CANCELED
    }
}