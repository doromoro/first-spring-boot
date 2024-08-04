package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class JpaMemberRepository implements MemberRepository{

    /*
    JPA는 EntityManager라는 녀석으로 동작한다.
     */
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    /*
    저장, 조회, 업데이트, 삭제 이런 SQL을 짤 필요가 없음.
    자동으로 된다.
    findById나 이런 것들은 알아서 되지만,
    PK가 아닌 것들은 jpql을 작성해야 한다.
    jpa기술을 스프링으로 감싸서 제공하는 것이 spring data jpa이다.
    그걸 사용하면 jpql을 작성하지 않아도 된다.
     */

    @Override
    public Member save(Member member) {
        // persist()는 영속시키다는 의미로 데이터를 저장하는 메서드
        em.persist(member);
        // method의 스펙을 맞추기 위해서 return
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // em.find()는 참조할 클래스(조회 타입 및 종류 명시)와 내가 찾을 값에 대해서 파라미터를 받음.
        // PK이기 떄문에 em.find()만으로 가져올 수 있음.
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        // Optional로 return하기 위해서 아래 코드 작성
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        /*
        여기서는 id와 같이 PK가 아니기 때문에 jpql이라는 객체지향 쿼리를 사용해야 함.
        sql과 비슷하지만 다르다.

        opt+cmd+n 커맨드를 통해서 inline method로 리팩토링 할 수 있음
        (ctrl+t -> inline variable 을 통해서도 가능)

        전달한 인자를 보면 테이블을 대상으로 쿼리를 날리는 것이 아니라,
        객체(Member), 정확히는 엔티티를 대상으로 쿼리를 넣는 모습이다.
        Member 뒤의 m은 as(alias)가 생략된 것이다.
        select 대상을 보면 Member 객체 자체를 select 한다.
        id, name이런 것들을 굳이 쓰지 않아도 이미 Entity에 매핑이 되어 있음.
        그래서 이거 한 줄로 끝난다.
        그러면 이게 SQL로 번역이 된다.
         */
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
