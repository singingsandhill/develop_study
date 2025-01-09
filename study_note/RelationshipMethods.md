# JPA의 연관관계 메서드 이해하기

JPA(Java Persistence API)는 객체지향적인 방식으로 데이터베이스와 상호작용을 가능하게 해주는 ORM(Object Relational Mapping) 기술입니다.
그중에서도 엔티티 간의 관계를 관리하는 "연관관계 메서드"는 데이터 일관성을 유지하고 코드의 가독성을 높이는 데 중요한 역할을 합니다.

---

## 1. 연관관계 메서드란?

연관관계 메서드는 엔티티 간의 관계를 설정하거나 해제할 때 사용되는 도우미 메서드입니다. 주로 **양방향 연관관계**에서 두 엔티티 간의 관계를 동기화하는 데 사용됩니다.

예를 들어, `Member`와 `Team` 엔티티가 양방향 연관관계(`@ManyToOne` - `@OneToMany`)로 매핑된 경우:

- `Member`는 `team` 필드를 가지고 있고,
- `Team`은 `members`라는 `List<Member>` 필드를 가집니다.

양방향 관계를 설정할 때, 한쪽 엔티티만 값을 세팅하면 다른 쪽은 갱신되지 않아 데이터가 불일치할 수 있습니다.

```java
Member member = new Member();
Team team = new Team();

// 연관관계 설정
member.setTeam(team); // Member의 team 필드만 설정됨
team.getMembers().add(member); // Team의 members 리스트에 추가
```

이처럼 두 엔티티 모두 관계를 설정해야 일관성이 유지됩니다. 이러한 과정을 간소화하기 위해 연관관계 메서드를 사용합니다.

---

## 2. 연관관계 메서드 구현하기

연관관계 메서드는 한쪽 엔티티에서 관계를 설정할 때, 다른 엔티티에도 자동으로 반영되도록 작성됩니다. 이를 통해 코드의 중복을 줄이고, 데이터 일관성을 유지할 수 있습니다.

### 예제 코드

#### Member 엔티티

```java
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // 연관관계 편의 메서드
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); // Team에도 Member 추가
    }
}
```

#### Team 엔티티

```java
@Entity
public class Team {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addMember(Member member) {
        members.add(member);
        member.setTeam(this); // Member의 team 설정
    }
}
```

위 코드에서는 `setTeam`과 `addMember` 메서드가 연관관계를 설정하고, 반대쪽 엔티티에도 동일한 관계를 적용합니다.

---

## 3. 연관관계 메서드 사용 시 주의사항

### 1) 양방향 관계의 주인 확인

JPA에서는 **연관관계의 주인**이 데이터베이스에 연관관계를 저장합니다. 연관관계 메서드는 주인 엔티티에서 작성하는 것이 일반적입니다.

- `@ManyToOne`은 주인 엔티티로, 실제 외래 키를 관리합니다.
- `@OneToMany`는 주인이 아니며 읽기 전용입니다.

### 2) 무한 루프 방지

연관관계 설정 시, 두 엔티티가 서로를 계속 호출하면 **StackOverflowError**가 발생할 수 있습니다.
이를 방지하기 위해 조건문을 추가하거나, 동일한 엔티티를 중복으로 설정하지 않도록 관리해야 합니다.

```java
public void addMember(Member member) {
    if (!members.contains(member)) {
        members.add(member);
        member.setTeam(this);
    }
}
```

### 3) 관계 초기화 확인

연관관계 필드는 초기화되지 않으면 `NullPointerException`이 발생할 수 있습니다. 컬렉션 필드는 항상 `new ArrayList<>()`로 초기화하는 것이 좋습니다.

---

## 4. 연관관계 메서드의 이점

1. **코드 가독성 향상**

   - 연관관계 설정 로직을 캡슐화하여 코드 중복을 줄이고, 가독성을 높입니다.

2. **데이터 일관성 유지**

   - 양방향 관계의 필드 값이 항상 동기화됩니다.

3. **생산성 증가**
   - 연관관계 설정 시 실수로 인한 오류를 줄일 수 있습니다.

---

## 5. 결론

연관관계 메서드는 JPA에서 엔티티 간의 관계를 효율적으로 관리하는 데 매우 중요한 역할을 합니다.
데이터 일관성을 유지하고, 코드의 유지보수성을 높이기 위해 반드시 활용해야 합니다.
