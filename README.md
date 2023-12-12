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

## 05/front interface 구현 (controller)

* 유저 생성시 -> 포인트 원장 생성 > 히스토리 추가

* plus, minus 이벤트 발생시 -> 포인트 가감 -> 히스토리 추가

* user service -> point service -> point history service 호출??

* 나중에 point에서 user 서비스가 필요하면, dependency cycle 발생!!

* kotlin에서는 null이 될수 있는 것(?표시, 변수 아님..val/var 차이), null이 될수 없는것(? 없는것) 차이가 있다.

* controller에서 validation 애노테셔션을 사용하기 위해서는 필수 값이더라도 nullable로 선언해 주어야 한다!!

    - 왜냐하면, not null 속성인데 null값이 들어오면, 컨트롤러 진입전에 에러가 발생한다!!

* valid 애노테이션 사용시 `@field:NotNull` 이러한 형태로 사용

* data class -> 롬복 사용필요 없음!!!  
 
* exception hanlder 적용전
```bash
curl -X POST -H "Content-type: application/json" http://localhost:8080/api/v1/user/create -d '{"email": "hello#world.com", "password": "test"}'

{"timestamp":"2023-12-12T13:52:53.822+00:00","status":400,"error":"Bad Request","path":"/api/v1/user/create"}
```

```bash
2023-12-12 22:52:53.816  WARN 26113 --- [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [0] in public me.project3.demo.common.inout.AppResponse<me.project3.demo.conroller.UserCreateOut> me.project3.demo.conroller.UserController.create(me.project3.demo.conroller.UserCreateIn) with 2 errors: [Field error in object 'userCreateIn' on field 'password': rejected value [test]; codes [Length.userCreateIn.password,Length.password,Length.java.lang.String,Length]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userCreateIn.password,password]; arguments []; default message [password],16,8]; default message [비밀번호는 8자 이상 16자 이하로 입력해주세요.]] [Field error in object 'userCreateIn' on field 'email': rejected value [hello#world.com]; codes [Email.userCreateIn.email,Email.email,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userCreateIn.email,email]; arguments []; default message [email],[Ljavax.validation.constraints.Pattern$Flag;@635d91fc,.*]; default message [이메일 형식이 아닙니다.]] ]
```

* exception hanlder 적용후
```bash
curl -X POST -H "Content-type: application/json" http://localhost:8080/api/v1/user/create -d '{"email": "hello#world.com", "password": "test"}'
{"message":"","error":"비밀번호는 8자 이상 16자 이하로 입력해주세요.","success":false,"notify":false,"data":null}
```

