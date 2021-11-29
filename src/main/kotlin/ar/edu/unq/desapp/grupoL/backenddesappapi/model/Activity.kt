package ar.edu.unq.desapp.grupoL.backenddesappapi.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "activities")
data class Activity(@Column var hour: LocalDateTime,
                    @OneToOne var cryptoCurrency: CryptoCurrency,
                    @ManyToOne var user: User,
                    @Column var amount: Double,
                    @Column var type: ActivityType) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0
}