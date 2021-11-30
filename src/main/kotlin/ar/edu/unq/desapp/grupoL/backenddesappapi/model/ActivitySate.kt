package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.ActivityAlreadyInTransactionException

enum class ActivitySate {
    POSTED {
        override fun startTransaction(activity: Activity) {
            activity.state = ActivitySate.IN_TRANSACTION
        }
    },

    IN_TRANSACTION{
        override fun startTransaction(activity: Activity): Unit = throw ActivityAlreadyInTransactionException()
    };

    abstract fun startTransaction(activity: Activity)
}