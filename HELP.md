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