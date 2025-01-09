package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext 생성자 어노테이션으로 대체 가능
    private final EntityManager em;

    /**
     * 저장하는 메서드
     * 커맨드와 쿼리를 분리하기 위해 Id만 조회(리턴)
     *
     * @param member
     * @return
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    /**
     * 단건 조회
     * @param id
     * @return
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 전체 조회
     * @return
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
    }
}
