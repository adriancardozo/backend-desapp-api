package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CreateTransactionDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.TransactionDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.ActivityNotFoundException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.InvalidOperationException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.TransactionNotFoundException
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.TransactionService
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.ErrorResponse
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.OkResponse
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@EnableAutoConfiguration
@Api(value = "transaction", description = "Rest API for transaction operations", tags = ["Transaction API"])
class TransactionRestService {
    @Autowired
    private lateinit var transactionService: TransactionService

    @PostMapping("/api/transaction/create/")
    @CrossOrigin
    fun createTransaction(@RequestHeader("authorization") token: String, @RequestBody createTransaction: CreateTransactionDTO): ResponseEntity<*> {
        return try {
            val transaction = transactionService.createTransaction(token, createTransaction)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(transaction)
        } catch (e: ActivityNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The activity not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/transaction/{id}/")
    @CrossOrigin
    fun getTransaction(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<TransactionDTO>(transactionService.transaction(token, id))
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @GetMapping("/api/transactions/")
    @CrossOrigin
    fun getTransactions(@RequestHeader("authorization") token: String): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<TransactionDTO>>(transactionService.userTransactions(token))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @PutMapping("/api/transaction/{id}/accept/")
    @CrossOrigin
    fun acceptTransaction(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            transactionService.accept(token, id)
            ResponseEntity.ok().body(OkResponse())
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @PutMapping("/api/transaction/{id}/reject/")
    @CrossOrigin
    fun rejectTransaction(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            transactionService.reject(token, id)
            ResponseEntity.ok().body(OkResponse())
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @PutMapping("/api/transaction/{id}/transfer/")
    @CrossOrigin
    fun transfer(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            transactionService.transfer(token, id)
            ResponseEntity.ok().body(OkResponse())
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @PutMapping("/api/transaction/{id}/commit/")
    @CrossOrigin
    fun commit(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            transactionService.commit(token, id)
            ResponseEntity.ok().body(OkResponse())
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }

    @PutMapping("/api/transaction/{id}/cancel/")
    @CrossOrigin
    fun cancel(@RequestHeader("authorization") token: String, @PathVariable id: Long): ResponseEntity<*> {
        return try {
            transactionService.cancel(token, id)
            ResponseEntity.ok().body(OkResponse())
        } catch (e: TransactionNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("The transaction not exists"))
        } catch (e: InvalidOperationException) {
            ResponseEntity.badRequest().body(ErrorResponse("Invalid operation"))
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }
}