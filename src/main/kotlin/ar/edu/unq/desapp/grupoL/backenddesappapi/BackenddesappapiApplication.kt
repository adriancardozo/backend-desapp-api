package ar.edu.unq.desapp.grupoL.backenddesappapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackenddesappapiApplication {
	companion object {
		@JvmStatic fun main(args : Array<String>) {
			runApplication<BackenddesappapiApplication>(*args)
		}
	}
}