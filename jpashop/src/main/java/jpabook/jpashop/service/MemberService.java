package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

//    의존성 주입 방법은 여러개
//    1. @Autowired + 필드 선언
//    @Autowired
//    private MemberRepository memberRepository;
//    2. 필드 선언 + setter
//    3. 필드 선언 + 생성자 (어노테이션으로 대체 가능)

    private final MemberRepository memberRepository; // final 넣으면 컴파일 시점에 에러 체크 가능

    /**
     * 회원가입
     *
     * @param member
     * @return
     */
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }

    }

    //회원 전체 조회
    @Transactional(readOnly = true) // 조회 성능 최적화
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
