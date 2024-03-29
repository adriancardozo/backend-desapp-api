package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.CreateActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.ActivityService
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ErrorResponseDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.OkResponseDTO
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@EnableAutoConfiguration
@Api(value = "activity", description = "Rest API for activity operations", tags = ["Activity API"])
class ActivityRestService {
    @Autowired
    private lateinit var activityService: ActivityService

    @GetMapping("/api/activities/")
    @CrossOrigin
    fun allActivities(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<ActivityDTO>>(activityService.allActivities())
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponseDTO("Bad Request"))
        }
    }

    @PostMapping("/api/activity/create/")
    @CrossOrigin
    fun createActivity(@RequestHeader("authorization") token: String, @RequestBody createActivity: CreateActivityDTO): ResponseEntity<*> {
        return try {
            activityService.createActivity(token, createActivity)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(OkResponseDTO())
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponseDTO("Bad Request"))
        }
    }
}