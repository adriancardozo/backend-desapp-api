package ar.edu.unq.desapp.grupoL.backenddesappapi.webservices

import ar.edu.unq.desapp.grupoL.backenddesappapi.dtos.ActivityDTO
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.ActivityService
import ar.edu.unq.desapp.grupoL.backenddesappapi.webservices.responses.ErrorResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ActivityRestService {
    @Autowired
    private lateinit var activityService: ActivityService

    @GetMapping("/api/activities/")
    @CrossOrigin
    fun allActivities(): ResponseEntity<*> {
        return try {
            ResponseEntity.ok().body<List<ActivityDTO>>(activityService.allActivities())
        } catch (e: Throwable) {
            ResponseEntity.badRequest().body(ErrorResponse("Bad Request"))
        }
    }
}