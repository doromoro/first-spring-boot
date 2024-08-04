package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    /**
     * spring이 구현체를 올려주게 된다.
     * @SpringBootTest 를 통해서 스프링과 연결되는데,
     * 실제로 테스트를 실행하면 스프링이 실행되고 테스트가 끝나면 스프링이 다시 내려간다.
     */
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;


    /**
     * test 코드는 production 코드로 나가는 것이 아니므로
     * 직관적으로 한글로 작성해도 괜찮다
     * 테스트 코드는 빌드될 때 포함되지도 않는다.
     *
     * 테스트는 given / when / then 형식으로 작성하는 것을 추천
     * 어떤 상황이 발생해서 이걸 실행했을 때 이게 나와야 해가 나눠져서 작성하는 것이 좋음.
     * 이러한 주석을 깔고 하는 것을 시작으로 하길 추천함. 어차피 이게 안맞는 상황도 있겠지만 대다수는 맞음.
     */
    @Test
    void 회원가입() {
        // given 어떤 데이터를 갖고
        Member member = new Member();
        member.setName("sk");

        // when -> spec 정의
        Long saveId = memberService.join(member);

        // then 회원가입이 되어있는지 리포지토리를 뒤져서 확인해봐야 할듯
        Member findMember = memberRepository.findById(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());

    }

    /**
     * 위의 테스트는 반쪽짜리 테스트임
     * 정상 테스트도 중요하지만 예외 테스트가 훨씬 중요하다.
     */
    @Test
    void 중복_회원가입_예외() {
        // given
        Member member1 = new Member();
        member1.setName("ksk");
        Member member2 = new Member();
        member2.setName("ksk");

        // when
        memberService.join(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("already use name");

    }

}