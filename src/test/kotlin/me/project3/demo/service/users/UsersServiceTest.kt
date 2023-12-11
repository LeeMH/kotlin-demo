package me.project3.demo.service.users


import io.github.serpro69.kfaker.Faker
import me.project3.demo.common.exception.AppException
import me.project3.demo.entity.users.Users
import me.project3.demo.entity.users.UsersRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("junit")
class UsersServiceTest {
    @Autowired
    lateinit var sut: IUsers

    @Autowired
    lateinit var repository: UsersRepository

    private val faker = Faker()

    @Test
    fun `유저 생성 테스트`() {
        // give
        val email = faker.internet.email()
        val password = faker.random.randomString(8)

        print(email)

        // when
        val user = sut.create(email, password)

        // then
        val fetchedUser = repository.getById(user.id)
        assertThat(fetchedUser.email).isEqualTo(email)
        assert(fetchedUser.password == password)
    }

    @Test
    fun `동일한 이메일이 존재한다면 에러가 발생해야 한다`() {
        // give
        val email = faker.internet.email()
        val password = faker.random.randomString(8)

        // when
        val user = sut.create(email, password)
        val ex = assertThrows<AppException> {
            sut.create(email, password)
        }

        // then
        assertThat(ex.message).contains(" 존재하는 email")
    }

    private fun createUser(email: String, password: String): Users.Vo {
        return sut.create(email, password)
    }
}