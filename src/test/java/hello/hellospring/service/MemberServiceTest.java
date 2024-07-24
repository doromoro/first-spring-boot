package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    /**
     * service에서도 객체를 새로 생성해서 쓰고 있어서 애매함.
     * 물론 지금 우리 저장소는 static으로 쓰고 있어서 상관은 없지만 기본적으로는 같은거 쓰고 싶잖아?
     * static 아니면 두 객체는 다르기 때문에 문제가 생길 수도 있음.
     * 이를 해결하기 위해 service 쪽에서 생성자를 하나 만들자. 리포지토리를 받는 생성자로
     */
    MemoryMemberRepository memberRepository;
    MemberService memberService;

    /**
     * 테스트 시작 전에 새로운 리포지토리를 만들고 서비스에도 이를 전달하면 같은 객체를 사용할 수 있게 된다.
     *
     */
    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        // 메모리 클리어
        memberRepository.clearStore();
    }

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
        // 1. 예외를 try-catch로 잡는 방법
//        try {
//            memberService.join(member2);
//            // 만약 다음줄이 실행되면 테스트 실패임.
//            fail();
//        }catch (IllegalStateException e){
//            // then
//            assertThat(e.getMessage()).isEqualTo("already use name");
//        }

        // 2. assertThrow로 예외를 테스트하는 법
        // 오르쪽의 람다 로직이 실행될 때 왼쪽의 예외가 발생해야 한다는 코드
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("already use name");

    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}