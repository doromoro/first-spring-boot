package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
1. 인터페이스가 인터페이스를 상속 받을 때는 extend 키워드 사용
2. Spring Data JPA에서 레포지토리 인터페이스는 JpaRepository<엔티티 타입, 식별자 타입> 를 상속 받아야 한다.
 */
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    /*
    놀랍게도 이게 다 만든거다.
    구현체도 없고 인터페이스만 있는데 그리고 뭐 만들지도 않았는데..?

    JpaRepository를 상속받으면 이 인터페이스의 구현체를 자동으로 만들어주고
    빈을 자동으로 등록해준다. 그걸 가져다 쓰는 것이다. -> SpringConfig로 이동
     */
    @Override
    Optional<Member> findByName(String name);
}
