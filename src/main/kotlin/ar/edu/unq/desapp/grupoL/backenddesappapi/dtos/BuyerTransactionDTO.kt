package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.TransactionState

data class BuyerTransactionDTO(val id: Long,
                               val activity: ActivityDTO,
                               val seller: SellerDTO,
                               val state: TransactionState
                               ): TransactionDTO {
}