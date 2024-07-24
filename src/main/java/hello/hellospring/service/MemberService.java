package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 테스트 패키지랑 파일을 만드는 가장 쉬운 방법은
 * 해당 클래스 위에서 cmd+shift+t를 누르는 것
 */

//@Service
public class MemberService {

    // 비즈니스 로직을 만드려면 일단 리포지토리가 필요함.
    private final MemberRepository memberRepository;

    /**
     * 이렇게 외부에서 다른 클래스를 넣어주는 이 모습을 DI(의존성 주입) 이라고 한다.
     */
//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // 회원가입, 반환값은 그 사람의 아이디 값
    public Long join(Member member){
        // 같은 이름이 있는 중복 회원은 막는 기능
        // cmd+opt+v로 변수 할당을 그냥 만들 수가 있음.
//        Optional<Member> result = memberRepository.findByName(member.getName());
        // isPresent()는 값이 있으면 true, 없으면 false
        // ifPresent()는 값이 있으면 다음 람다함수를 실행, 지금의 경우 중복회원을 찾으려고 하는 것이므로 예외처리 구문 실행
//        result.ifPresent(m -> {
//            throw new IllegalStateException("already use name");
//        });
        
        //코드 정리
        // 이렇게 로직이 쭉 나오는 경우에는 메서드로 분리해주는 것이 좋다.
        vallidateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    // cmd+opt+m 키로 refactor(extract method)
    /**
     * 이 메서드를 테스트하는 것은 여러가지 방법이 있다.
     * 애플리케이션을 실행시켜서 할 수도 있고 DB에 값이 들어왔는지 확인하는 것도 답이 된다.
     * 그러나 제일 좋은 것은 테스트 케이스를 작성하는 것이다.
     */
    private void vallidateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                        .ifPresent(m -> {
                            throw new IllegalStateException("already use name");
                        });
    }

    // 전체 회원 조회
    /**
     * 리포지토리는 메서드 명을 보면 그냥 DB 제어하는 느낌이 강하다면 (개발느낌에 더 강하게 작성)
     * 서비스는 메서드 명이 비즈니스에 좀 더 가깝다.
     * 서비스 클래스는 대부분 비즈니스 용어를 사용해야 한다.(비즈니스 의존적으로 설정)
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}
