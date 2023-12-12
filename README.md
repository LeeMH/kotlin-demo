# Kotlin 101

## 프로젝트 구현 내용

* 간단한 회원 가입

* 포인트 원장 구현

* 회원 : 포인트 = 1 : 1 관계

* 포인트 : 포인트 내역 = 1 : N 관계

## 01/entity-define

* primary 생성자, 멤버 변수

* named parameter

* 변하는 것과 변하지 않는것

## 02/user service 생성

* kotlin에서 == 은 java에서 equals와 같다.

* !! : null이 아님을 보장

* 외부로 노출할때는 read only(value object)로 내어 준다.

* find = 없을수 있다, get = 없으면 예외를 던진다.

```kotlin
    override fun findByEmail(email: String): Users.Vo? {
        return repository.findByEmail(email)?.toVo()
    }

    override fun getByEmail(email: String): Users.Vo {
        return repository.findByEmail(email)?.toVo()
            ?: throw AppException("존재하지 않는 user email 입니다. email: $email")
    }
```

* null safety

```kotlin    
    override fun getById(id: Long): Users.Vo {
        return repository.findByIdOrNull(id)?.toVo()
            ?: throw AppException("존재하지 않는 user id 입니다. id: $id")
        
    }
```

```java
        var user = repository.findById(id);
        
        if (user == null) {
            throw new AppException("존재하지 않는 user id 입니다. id: $id");
        } else {
            return user.toVo();
        }
```


## 03/point service 생성

* named parameter & default value

```kotlin
    override fun create(point: Point.Vo, amount: Long): PointHistory.Vo {
        val history = PointHistory(
            userId = point.userId
        )

        return repository.save(history).toVo()
    }

class PointHistory(
    // 회원 id
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    // 포인트 거래 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(16)", nullable = false)
    val type: Point.Type = Point.Type.CREATED,

    // 포인트 금액
    @Column(name = "amount", nullable = false)
    var amount: Long = 0,

    // 변경전 포인트
    @Column(name = "bef_bal", nullable = false)
    var beforeBalance: Long = 0,

    // 변경후 포인트
    @Column(name = "bal", nullable = false)
    var balance: Long = 0,
) 
```

## 04/point service 테스트 코드 추가

* 간단한 로직이더라도 추가하면서, 에러를 찾아낼 수 있다

* 생각해볼만 내용

    - 생각보다 많은 코드가 중복된다. given, when, then 구조에서 발생할수 밖에 없는 문제점 -> 리펙토링
    - 소스 = 72 라인 , 테스트코드 = 133 라인
    - 예외 메세지가 변경된다면??

