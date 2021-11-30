package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "activities")
data class Activity(@Column var hour: LocalDateTime,
                    @OneToOne(cascade = [CascadeType.PERSIST]) var quotation: Quotation,
                    @ManyToOne var user: User,
                    @Column var amount: Double,
                    @Column var type: ActivityType,
                    @Column var state: ActivitySate = ActivitySate.POSTED) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0

    fun startTransaction() = state.startTransaction(this)

    fun isPosted(): Boolean = state == ActivitySate.POSTED
}