package me.project3.demo.service.point

import io.github.serpro69.kfaker.Faker
import me.project3.demo.common.inout.AppException
import me.project3.demo.entity.point.Point
import me.project3.demo.entity.point.PointRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("junit")
class PointServiceTest {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    lateinit var sut: IPoint

    @Autowired
    lateinit var repository: PointRepository

    private val faker = Faker()

    @Test
    fun `포인트 원장 생성 테스트`() {
        // give
        val userId = faker.random.nextLong()

        // when
        val point = sut.create(userId)

        // then
        val fetchedPoint = repository.getById(point.userId)
        assertThat(fetchedPoint.userId).isEqualTo(userId)
        assertThat(fetchedPoint.balance).isEqualTo(0)
        assertThat(fetchedPoint.beforeBalance).isEqualTo(0)
    }

    @Test
    fun `이미 생성된 유저의 원장 생성시 에러가 발생해야 한다`() {
        // give
        val userId = faker.random.nextLong()

        // when
        val point = sut.create(userId)
        val ex = assertThrows<AppException> {
            sut.create(userId)
        }

        // then
        log.info("exception message: ${ex.message}")
        assertThat(ex.message).contains("이미 존재하는 user id")
    }

    @Test
    fun `포인트 plus-minus 호출시 부호는 모두 양수이고 0이상의 숫자가 입력되어야 한다`() {
        // give
        val userId = faker.random.nextLong()

        // when
        val point = sut.create(userId)
        val plusException = assertThrows<AppException> {
            sut.plus(userId, 0, Point.Type.REWARDED)
        }

        val minusException = assertThrows<AppException> {
            sut.plus(userId, 0, Point.Type.USED)
        }

        // then
        assertThat(plusException.message).contains("포인트는 0보다 커야합니다")
        assertThat(minusException.message).contains("포인트는 0보다 커야합니다")
    }

    @Test
    fun `포인트 plus 요청시 포인트가 추가되어야 한다`() {
        // give
        val userId = faker.random.nextLong()

        // when
        sut.create(userId)
        val point1 = sut.plus(userId, 1, Point.Type.REWARDED)
        val point2 = sut.plus(userId, 10, Point.Type.REWARDED)

        // then
        assertThat(point1.balance).isEqualTo(1)
        assertThat(point1.beforeBalance).isEqualTo(0)

        assertThat(point2.balance).isEqualTo(11)
        assertThat(point2.beforeBalance).isEqualTo(1)
    }

    @Test
    fun `포인트 minus 요청시 포인트가 추가되어야 한다`() {
        // give
        val userId = faker.random.nextLong()

        // when
        sut.create(userId)
        val point1 = sut.plus(userId, 100, Point.Type.REWARDED)
        val point2 = sut.minus(userId, 1, Point.Type.USED)

        // then
        assertThat(point1.balance).isEqualTo(100)
        assertThat(point1.beforeBalance).isEqualTo(0)

        assertThat(point2.balance).isEqualTo(99)
        assertThat(point2.beforeBalance).isEqualTo(100)
    }

    @Test
    fun `포인트 부족시 minus 에러가 발생해야 한다`() {
        // give
        val userId = faker.random.nextLong()

        // when
        sut.create(userId)
        sut.plus(userId, 100, Point.Type.REWARDED)
        val ex = assertThrows<AppException> {
            sut.minus(userId, 101, Point.Type.USED)
        }

        // then
        assertThat(ex.message).contains("부족")
    }
}