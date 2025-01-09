package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    // @Rollback(false) // commit 될 때 쿼리 실행, insert문 실행 안 되고 진행
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Gim");

        //when
        Long sevedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(sevedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {

        //given
        Member member1 = new Member();
        member1.setName("Gim");

        Member member2 = new Member();
        member2.setName("Gim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        // expected = IllegalStateException.class으로 생략
//        try {
//            memberService.join(member2); // 중복 오류 발생해야 함
//        } catch (IllegalStateException e) {
//            return;
 //       }

        //then
        fail("예외 발생해야 함");
    }

}