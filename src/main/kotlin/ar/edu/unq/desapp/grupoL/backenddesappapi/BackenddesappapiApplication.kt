package ar.edu.unq.desapp.grupoL.backenddesappapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class BackenddesappapiApplication

fun main(args: Array<String>) {
	runApplication<BackenddesappapiApplication>(*args)
}
