package ar.edu.unq.desapp.grupoL.backenddesappapi

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTests {
    @Autowired
    private lateinit var userService: UserService

    @Test
    fun whenAUserIsSavedItCanBeRecovered() {
        val user = aUser()
        val idUserSaved = userService.save(user).id
        val recoveredUser = userService.findByID(idUserSaved)
        assertEquals(user, recoveredUser)
    }

    fun aUser(): User {
        return User("UserName", "UserLastname", "user@gmail.com", "UserAddress", "UserPassword", "1111111111111111111111", "11111111", 10, 2)
    }
}
