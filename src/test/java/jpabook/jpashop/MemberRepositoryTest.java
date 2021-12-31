package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    public void save() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("test");

        // when
        Long saveId = memberRepository.save(member);
        Member getMember = memberRepository.find(saveId);

        // then
        assertThat(saveId).isEqualTo(getMember.getId());
        assertThat(member.getUsername()).isEqualTo(getMember.getUsername());
    }
}