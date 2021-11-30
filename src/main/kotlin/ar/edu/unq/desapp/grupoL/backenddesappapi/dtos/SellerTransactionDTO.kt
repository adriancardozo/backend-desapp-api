package ar.edu.unq.desapp.grupoL.backenddesappapi.dtos

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.TransactionState

data class SellerTransactionDTO(val id: Long,
                                val activity: ActivityDTO,
                                val buyer: BuyerDTO,
                                val state: TransactionState
                                ): TransactionDTO {
}