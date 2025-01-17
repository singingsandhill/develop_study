# 영속성 컨텍스트(Persistence Context)

**영속성 컨텍스트(Persistence Context)**는 JPA(Java Persistence API)에서 매우 중요한 개념으로, 엔티티를 영속 상태로 관리하는 환경입니다. <br>
쉽게 말해, 애플리케이션과 데이터베이스 사이에 존재하는 중간 메모리 공간이라고 이해할 수 있습니다.

## 1. 영속성 컨텍스트의 정의

JPA 엔티티(Entity)를 관리하는 메모리 공간으로, 엔티티의 생명 주기를 관리합니다.<br>
엔티티 매니저(EntityManager)를 통해 접근하며, 애플리케이션에서 엔티티 객체와 데이터베이스 사이의 중개 역할을 합니다.

## 2. 영속성 컨텍스트의 특징

### (1) 엔티티의 생명 주기 관리

엔티티 객체는 다음 네 가지 생명 주기를 가집니다:

- 비영속 (Transient): 아직 영속성 컨텍스트에 저장되지 않은 상태.
- 영속 (Persistent): 영속성 컨텍스트에서 관리되는 상태.
- 준영속 (Detached): 영속성 컨텍스트에서 분리된 상태.
- 삭제 (Removed): 데이터베이스에서 삭제가 예약된 상태.

### (2) 1차 캐시

영속성 컨텍스트는 1차 캐시를 제공합니다.<br>
즉, 특정 엔티티를 조회하면 데이터베이스에 바로 접근하지 않고, 먼저 메모리(1차 캐시)에서 조회합니다.<br>
만약 1차 캐시에 없다면 데이터베이스에서 조회 후 캐시에 저장합니다.

### (3) 동일성 보장

같은 영속성 컨텍스트 내에서 동일한 엔티티를 조회하면 항상 같은 객체 인스턴스를 반환합니다.<br>
이는 애플리케이션에서 엔티티를 효율적으로 관리할 수 있도록 돕습니다.

```java
Member member1 = em.find(Member.class, 1L);
Member member2 = em.find(Member.class, 1L);

System.out.println(member1 == member2); // true (동일 객체)
```

### (4) 변경 감지 (Dirty Checking)

영속성 컨텍스트는 엔티티 객체의 변경을 감지합니다.<br>
트랜잭션이 커밋되기 전 변경된 엔티티를 자동으로 데이터베이스에 반영(UPDATE)합니다.

```java
Member member = em.find(Member.class, 1L); // 영속 상태
member.setName("New Name"); // 변경 감지
// 트랜잭션 커밋 시 UPDATE 쿼리 자동 실행
```

### (5) 지연 쓰기 (Write-Behind)

트랜잭션이 끝날 때까지 데이터베이스와의 직접적인 통신을 최소화합니다.<br>
INSERT, UPDATE, DELETE 쿼리는 트랜잭션 커밋 시점에 한꺼번에 처리됩니다.

## 3. 영속성 컨텍스트의 주요 동작

### (1) persist()

엔티티를 영속성 컨텍스트에 저장합니다.<br>
데이터베이스에 바로 저장되지 않고, 트랜잭션 커밋 시 반영됩니다.

```java
Member member = new Member();
member.setName("John Doe");
em.persist(member); // 영속 상태로 전환
```

### (2) find()

엔티티를 영속성 컨텍스트에서 조회합니다.<br>
1차 캐시에 있으면 캐시에서 반환, 없으면 데이터베이스에서 조회 후 1차 캐시에 저장합니다.

```java
Member member = em.find(Member.class, 1L);
```

### (3) remove()

엔티티를 삭제 상태로 표시합니다.
트랜잭션 커밋 시점에 DELETE 쿼리가 실행됩니다.

```java
Member member = em.find(Member.class, 1L);
em.remove(member); // 삭제 상태
```

### (4) detach()

엔티티를 영속성 컨텍스트에서 분리합니다.<br>
이후 변경 감지가 일어나지 않습니다.

```java
Member member = em.find(Member.class, 1L);
em.detach(member); // 준영속 상태로 전환
```

### 4. 영속성 컨텍스트의 장점

- 성능 최적화: 1차 캐시를 통해 데이터베이스 접근 횟수를 줄임.
- 트랜잭션 관리: 변경 감지를 통해 객체 상태를 자동으로 데이터베이스와 동기화.
- 동일성 보장: 동일한 영속성 컨텍스트 내에서는 항상 같은 객체를 반환.
- 지연 쓰기: 트랜잭션 종료 시점까지 쿼리 실행을 지연하여 효율적인 처리.

## 5. 주의점

영속성 컨텍스트의 크기 관리: 많은 엔티티를 영속성 컨텍스트에 담으면 메모리 사용량이 증가하므로, 필요하지 않은 엔티티는 detach()하거나 clear()를 호출하여 관리해야 합니다.<br>
준영속 상태 주의: 준영속 상태의 엔티티는 변경 감지가 일어나지 않으므로 데이터베이스와 동기화되지 않습니다.

## 6. 영속성 컨텍스트와 엔티티 매니저

영속성 컨텍스트는 엔티티 매니저(EntityManager)에 의해 관리됩니다.<br>
하나의 엔티티 매니저는 하나의 영속성 컨텍스트를 가집니다.

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
EntityManager em = emf.createEntityManager(); // 새로운 영속성 컨텍스트 생성
```

## 요약

영속성 컨텍스트는 JPA의 핵심 기능으로, 엔티티를 메모리에서 관리하고 데이터베이스와 동기화하는 역할을 합니다. <br>
이를 통해 애플리케이션은 효율적인 데이터 관리와 변경 감지 등의 기능을 제공받을 수 있습니다. 하지만 사용 시 메모리 관리와 상태 전환에 주의해야 효율적인 애플리케이션을 개발할 수 있습니다.
