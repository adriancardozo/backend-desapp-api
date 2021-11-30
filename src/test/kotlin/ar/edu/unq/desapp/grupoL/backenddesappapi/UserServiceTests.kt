package ar.edu.unq.desapp.grupoL.backenddesappapi

import ar.edu.unq.desapp.grupoL.backenddesappapi.model.User
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.UserService
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserAlreadyExistsException
import ar.edu.unq.desapp.grupoL.backenddesappapi.exceptions.UserNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTests {
    @Autowired
    private lateinit var userService: UserService
    private lateinit var user: User
    private lateinit var userNotFound: User

    @BeforeEach
    fun beforeEach() {
        user = aUser()
        userNotFound = aUser()
    }

    @Test
    @Transactional
    fun whenAUserIsSavedItCanBeRecoveredById() {
        val idUserSaved = userService.save(user).id
        val recoveredUser = userService.findByID(idUserSaved)
        assertEquals(user, recoveredUser)
    }

    @Test
    @Transactional
    fun whenAUserIsSavedItCanBeRecoveredByEmail() {
        val emailUserSaved = userService.save(user).email
        val recoveredUser = userService.findByEmail(emailUserSaved)
        assertEquals(user, recoveredUser)
    }

    @Test
    fun cantCreateTwoUsersWithTheSameEmail() {
        userService.save(user)
        assertThrows<UserAlreadyExistsException> { userService.save(user) }
    }

    @Test
    fun cantGetByIdANonSavedUser() {
        assertThrows<UserNotFoundException> { userService.findByID(userNotFound.id) }
    }

    @Test
    fun cantGetByEmailANonSavedUser() {
        assertThrows<UserNotFoundException> { userService.findByEmail(userNotFound.email) }
    }

    fun aUser(email: String = "user@gmail.com"): User {
        return User("UserName", "UserLastname", email, "UserAddress", "UserPassword", "1111111111111111111111", "11111111", 10, 2)
    }
}
